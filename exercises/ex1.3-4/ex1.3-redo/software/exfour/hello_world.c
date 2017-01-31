#include <stdio.h>
#include <unistd.h>
#include <string.h>

#define USLEEP_SEC 1000000

#define RS232_CONTROL 	(*(volatile unsigned char *) (0x84000200))
#define RS232_STATUS 	(*(volatile unsigned char *) (0x84000200))
#define RS232_TXDATA 	(*(volatile unsigned char *) (0x84000202))
#define RS232_RXDATA 	(*(volatile unsigned char *) (0x84000202))
#define RS232_BAUD 		(*(volatile unsigned char *) (0x84000204))

#define TOUCH_CONTROL 	(*(volatile unsigned char *) (0x84000230))
#define TOUCH_STATUS 	(*(volatile unsigned char *) (0x84000230))
#define TOUCH_TXDATA 	(*(volatile unsigned char *) (0x84000232))
#define TOUCH_RXDATA 	(*(volatile unsigned char *) (0x84000232))
#define TOUCH_BAUD 		(*(volatile unsigned char *) (0x84000234))

#define BT_CONTROL 	(*(volatile unsigned char *) (0x84000220))
#define BT_STATUS 	(*(volatile unsigned char *) (0x84000220))
#define BT_TXDATA 	(*(volatile unsigned char *) (0x84000222))
#define BT_RXDATA 	(*(volatile unsigned char *) (0x84000222))
#define BT_BAUD 	(*(volatile unsigned char *) (0x84000224))

/* a data type to hold a point/coordinate */
typedef struct {
	int x, y;
} Point;

// Touch screen functions
void initTouch(void);
int screenTouched(void);
void waitForTouch();
Point getPress(void);
Point getRelease(void);

// General purpose functions
int getChar(volatile unsigned char* status, volatile unsigned char* rx);
int putChar(volatile unsigned char* status, volatile unsigned char* tx, int c);
// sends a string to the serial tx, string must be terminated by \0, \0 is not sent
void putString(volatile unsigned char* status, volatile unsigned char* tx,
		char *string);

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
// ###########################################################################
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
// ###########################################################################
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

void bluetoothListen(){
	while (1){
		int x = getChar(&BT_STATUS, &BT_RXDATA);
		printf("%d",x);
	}
}

