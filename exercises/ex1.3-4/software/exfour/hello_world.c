#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <main.h>

/* a data type to hold a point/coordinate */
typedef struct {
	int x, y;
} Point;

// General purpose functions
int getChar(volatile unsigned char* status, volatile unsigned char* rx);
int putChar(volatile unsigned char* status, volatile unsigned char* tx, int c);
// sends a string to the serial tx, string must be terminated by \0, \0 is not sent
void putString(volatile unsigned char* status, volatile unsigned char* tx,
		char *string);

// Touch screen functions
void initTouch(void);
int screenTouched(void);
void waitForTouch();
Point getPress(void);
Point getRelease(void);

// Bluetooth functions
void initBluetooth(void);
void setBluetoothName(char *name);
void setBluetoothPassword(char *password);

int main() {
	printf("Hello from Nios II!\n");

	initBluetooth();

	/*// use to test touch screen
	 initTouch();
	 Point p;
	 while (1) {
	 p = getPress();
	 printf("press %d, %d\n", p.x, p.y);
	 p = getRelease();
	 printf("release %d, %d\n", p.x, p.y);
	 }
	 */
	return 0;
}

int getChar(volatile unsigned char* status, volatile unsigned char* rx) {
	while (!(*status & 0x1))
		;
	int result = *rx;
	return result & 255;
}

int putChar(volatile unsigned char* status, volatile unsigned char* tx, int c) {
	while (!(*status & 0x2))
		;
	*tx = c;
	return c; // return c
}

void putString(volatile unsigned char* status, volatile unsigned char* tx,
		char *string) {
	char c;
	int i = 0;
	sendnextchar: c = string[i];
	if (c == '\0')
		return;
	putChar(status, tx, (int) c);
	i++;
	goto sendnextchar;
}

// ###########################################################################
// ############################# TOUCH SCREEN ################################
// ###########################################################################

/*****************************************************************************
 ** Initialise touch screen controller
 *****************************************************************************/
void initTouch(void) {
	// Program 6850 and baud rate generator to communicate with touchscreen
	// send touchscreen controller an "enable touch" command

	// setting up the 6850
	TOUCH_CONTROL = 0x95;
	TOUCH_BAUD = 0x07;

	// now that it is set up, we talk to the touch controller through rx & tx
	putChar(&TOUCH_STATUS, &TOUCH_TXDATA, 0x55);
	putChar(&TOUCH_STATUS, &TOUCH_TXDATA, 0x01);
	putChar(&TOUCH_STATUS, &TOUCH_TXDATA, 0x12);

	while (getChar(&TOUCH_STATUS, &TOUCH_RXDATA) != 0)
		; // wait for OK

	printf("touch screen initialized\n");
}

/*****************************************************************************
 ** test if screen touched
 *****************************************************************************/
int ScreenTouched(void) {
	// return TRUE if any data received from 6850 connected to touchscreen
	// or FALSE otherwise
	return getChar(&TOUCH_STATUS, &TOUCH_RXDATA) == 129;
}

/*****************************************************************************
 ** wait for screen to be touched
 *****************************************************************************/
void waitForTouch() {
	while (!ScreenTouched())
		;
}

/*****************************************************************************
 * This function waits for a touch screen press event and returns X,Y coord
 *****************************************************************************/
Point getPress(void) {

	Point p1;
	char bytes[4];
	while (getChar(&TOUCH_STATUS, &TOUCH_RXDATA) != 129)
		;
	int i;
	for (i = 0; i < 4; i++)
		bytes[i] = getChar(&TOUCH_STATUS, &TOUCH_RXDATA);
	p1.x = 480 * ((int) bytes[0] + (int) (bytes[1] << 7)) / 4096.0;
	p1.y = 800 * ((int) bytes[2] + (int) (bytes[3] << 7)) / 4096.0;
	return p1;
}

/*****************************************************************************
 * This function waits for a touch screen release event and returns X,Y coord
 *****************************************************************************/
Point getRelease(void) {

	Point p1;
	char bytes[4];
	while (getChar(&TOUCH_STATUS, &TOUCH_RXDATA) != 128)
		;
	int i;
	for (i = 0; i < 4; i++)
		bytes[i] = getChar(&TOUCH_STATUS, &TOUCH_RXDATA);
	p1.x = 480 * ((int) bytes[0] + (int) (bytes[1] << 7)) / 4096.0;
	p1.y = 800 * ((int) bytes[2] + (int) (bytes[3] << 7)) / 4096.0;
	return p1;
}

// ###########################################################################
// ############################### BLUETOOTH #################################
// ###########################################################################

/*****************************************************************************
 ** Initialise the bluetooth controller
 *****************************************************************************/
void initBluetooth(void) {

	// setting up the 6850
	BT_CONTROL = 0x95;	// 8 bits, no parity, 1 stop bit
	BT_BAUD = 0x01;	// 115k baud
	setBluetoothName("Group15");
	setBluetoothPassword("cpen391");
	printf("Bluetooth initialized!\n");
	bluetoothListen();
}

void setBluetoothName(char *name) {
	// wait a second before and after entering command mode
	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "$$$"); // enter command mode
	usleep(USLEEP_SEC);

	// send the command to change the name
	char command[25] = "SN,";
	strcat(command, name);
	strcat(command, "\r\n");

	putString(&BT_STATUS, &BT_TXDATA, command);


	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "---"); // enter command mode
	usleep(USLEEP_SEC);
}

void setBluetoothPassword(char *password) {
	// wait a second before and after entering command mode
	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "$$$"); // enter command mode
	usleep(USLEEP_SEC);

	// send the command to change the name
	char command[25] = "SP,";
	strcat(command, password);
	strcat(command, "\r\n");

	putString(&BT_STATUS, &BT_TXDATA, command);


	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "---"); // enter command mode
	usleep(USLEEP_SEC);
}

void bluetoothListen() {
	while (1){
		int x = getChar(&BT_STATUS, &BT_RXDATA);
		printf("%d",x);
	}
}

// ###########################################################################
// ################################ GRAPHICS #################################
// ###########################################################################

/*******************************************************************************************
* This function writes a single pixel to the x,y coords specified using the specified colour
* Note colour is a byte and represents a palette number (0-255) not a 24 bit RGB value
********************************************************************************************/
void WriteAPixel(int x, int y, int Colour) {
	WAIT_FOR_GRAPHICS; // is graphics ready for new command
	GraphicsX1Reg = x; // write coords to x1, y1
	GraphicsY1Reg = y;
	GraphicsColourReg = Colour; // set pixel colour
	GraphicsCommandReg = PutAPixel; // give graphics "write pixel" command
}

/*********************************************************************************************
* This function read a single pixel from the x,y coords specified and returns its colour
* Note returned colour is a byte and represents a palette number (0-255) not a 24 bit RGB value
*********************************************************************************************/
int ReadAPixel(int x, int y) {
	WAIT_FOR_GRAPHICS; // is graphics ready for new command
	GraphicsX1Reg = x // write coords to x1, y1
	GraphicsY1Reg = y;
	GraphicsCommandReg = GetAPixel; // give graphics a "get pixel" command
	WAIT_FOR_GRAPHICS; // is graphics done reading pixel
	return (int)(GraphicsColourReg) ; // return the palette number (colour)
}

/**********************************************************************************
** subroutine to program a hardware (graphics chip) palette number with an RGB value
** e.g. ProgramPalette(RED, 0x00FF0000) ;
**
************************************************************************************/
void ProgramPalette(int PaletteNumber, int RGB) {
	WAIT_FOR_GRAPHICS;
	GraphicsColourReg = PaletteNumber;
	GraphicsX1Reg = RGB >> 16 ; // program red value in ls.8 bit of X1 reg
	GraphicsY1Reg = RGB ; // program green and blue into ls 16 bit of Y1 reg
	GraphicsCommandReg = ProgramPaletteColour; // issue command
}

/*********************************************************************************************
This function draw a horizontal line, 1 pixel at a time starting at the x,y coords specified
*********************************************************************************************/
void HLine(int x1, int y1, int length, int Colour) {
	int i;
	for(i = x1; i < x1+length; i++ )
	WriteAPixel(i, y1, Colour);
}

/*********************************************************************************************
This function draw a vertical line, 1 pixel at a time starting at the x,y coords specified
*********************************************************************************************/
void VLine(int x1, int y1, int length, int Colour) {
	int i;
	for(i = y1; i < y1+length; i++ )
	WriteAPixel(x1, i, Colour);
}

/*******************************************************************************
** Implementation of Bresenhams line drawing algorithm
*******************************************************************************/
void Line(int x1, int y1, int x2, int y2, int Colour) {
	int x = x1;
	int y = y1;
	int dx = abs(x2 - x1);
	int dy = abs(y2 - y1);

	int s1 = sign(x2 - x1);
	int s2 = sign(y2 - y1);
	int i, temp, interchange = 0, error ;

	// if x1=x2 and y1=y2 then it is a line of zero length so we are done
	if(dx == 0 && dy == 0) {
		return ;
	} else { // must be a complex line so use Bresenhams algorithm
	// swap delta x and delta y depending upon slop of line
		if(dy > dx) {
			temp = dx ;
			dx = dy ;
			dy = temp ;
			interchange = 1 ;
		}

		// initialise the error term to compensate for non-zero intercept
		error = (dy << 1) - dx ; // error = (2 * dy) - dx

		// main loop
		for(i = 1; i <= dx; i++) {
			WriteAPixel(x, y, Colour);

			while(error >= 0) {
				if(interchange == 1)
				x += s1 ;
				else
				y += s2 ;
				error -= (dx << 1) ; // error = error – (dx * 2)
			}

			if(interchange == 1) {
				y += s2 ;
			}
			else {
				x += s1 ;
			}
			error += (dy << 1) ; // error = error + (dy * 2)
		}
	}
 }
