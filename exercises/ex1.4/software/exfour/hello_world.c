#include <stdio.h>

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

#define leds 		(*(volatile unsigned char *) (0x00002010))

/* a data type to hold a point/coord */
typedef struct {
	int x, y;
} Point;

// RS232 functions
void Init_RS232(void);
int putcharRS232(int c);
int getcharRS232(void);
int RS232TestForReceivedData(void);

// Touch screen functions
void Init_Touch(void);
int ScreenTouched(void);
void WaitForTouch();
Point GetPress(void);
Point GetRelease(void);

int main() {
	printf("Hello from Nios II!\n");

	//Init_RS232();
	Init_Touch();

	Point p;
	while(1) {
		p = GetPress();
		printf("%d, %d", p.x, p.y);
		p = GetRelease();
		printf("%d, %d", p.x, p.y);
	}

	return 0;
}

// ###########################################################################
// ###########################################################################
// ###########################################################################

/**************************************************************************
 Subroutine to initialise the RS232 Port by writing some data
 ** to the internal registers.
 ** Call this function at the start of the program before you attempt
 ** to read or write to data via the RS232 port
 **
 ** Refer to 6850 data sheet for details of registers and
 ***************************************************************************/
void Init_RS232(void) {
	// set up 6850 Control Register to utilise a divide by 16 clock,
	// set RTS low, use 8 bits of data, no parity, 1 stop bit,
	// transmitter interrupt disabled
	// program baud rate generator to use 115k baud
	RS232_CONTROL = 0x55;
	RS232_BAUD = 0x01;
}

int putcharRS232(int c) {
	// poll Tx bit in 6850 status register. Wait for it to become '1'
	// write 'c' to the 6850 TxData register to output the character
	while (!(RS232_STATUS & 0x2))
		;
	RS232_TXDATA = c;
	return c; // return c
}

int getcharRS232(void) {
	// poll Rx bit in 6850 status register. Wait for it to become '1'
	// read received character from 6850 RxData register.
	while (!(RS232_STATUS & 0x1))
		;
	int result = RS232_RXDATA;
	return result;
}

// the following function polls the 6850 to determine if any character
// has been received. It doesn't wait for one, or read it, it simply tests
// to see if one is available to read
int RS232TestForReceivedData(void) {
	// Test Rx bit in 6850 serial comms chip status register
	// if RX bit is set, return TRUE, otherwise return FALSE
	return (RS232_STATUS & 0x01);
}

// ###########################################################################
// ###########################################################################
// ###########################################################################

/*****************************************************************************
 ** Initialise touch screen controller
 *****************************************************************************/
void Init_Touch(void) {
	// Program 6850 and baud rate generator to communicate with touchscreen
	// send touchscreen controller an "enable touch" command

	// setting up the 6850
	TOUCH_CONTROL = 0x95;
	TOUCH_BAUD = 0x07;

	// now that it is set up, we talk to the touch controller through rx & tx
	char s = TOUCH_STATUS;
	while(TOUCH_STATUS & 0x02 == 0x00);
	//Wait_Ready_W(TOUCH_STATUS);
	TOUCH_TXDATA = 0x55;
	while(TOUCH_STATUS & 0x02 == 0x00);
	TOUCH_TXDATA = 0x01;
	while(TOUCH_STATUS & 0x02 == 0x00);
	TOUCH_TXDATA = 0x12; // TOUCH_ENABLE

	printf("touch screen initialized\n");
}

/*****************************************************************************
 ** test if screen touched
 *****************************************************************************/
int ScreenTouched(void) {
	// return TRUE if any data received from 6850 connected to touchscreen
	// or FALSE otherwise
	return (TOUCH_STATUS & 0x01 == 0x01);
}

/*****************************************************************************
 ** wait for screen to be touched
 *****************************************************************************/
void WaitForTouch() {
	while (!ScreenTouched()) {
		;
	}
}

/*****************************************************************************
 * This function waits for a touch screen press event and returns X,Y coord
 *****************************************************************************/
Point GetPress(void) {
	Point p1;
	// wait for a pen down command then return the X,Y coord of the point
	// calibrated correctly so that it maps to a pixel on screen

	char bytes[4];

	WaitForTouch();
	char rxData = TOUCH_RXDATA;
	while(!((rxData & 128) && (rxData & 1))) { // wait for start of down packet
		rxData = TOUCH_RXDATA;
	}

	int i;
	for (i = 0; i < 4; i++) {
		while(TOUCH_STATUS & 0x02 == 0x00);
		bytes[i] = TOUCH_RXDATA;
	}

	p1.x = bytes[0] + bytes[1];
	p1.y = bytes[2] + bytes[3];

	return p1;
}

/*****************************************************************************
 * This function waits for a touch screen release event and returns X,Y coord
 *****************************************************************************/
Point GetRelease(void) {
	Point p1;
	// wait for a pen up command then return the X,Y coord of the point
	// calibrated correctly so that it maps to a pixel on screen

	char bytes[4];

	WaitForTouch();
	char rxData = TOUCH_RXDATA;

	while(!((rxData & 128) && !(rxData & 1))) { // wait for start of up packet
		rxData = TOUCH_RXDATA;
	}

	int i;
	for (i = 0; i < 4; i++) {
		while(TOUCH_STATUS & 0x02 == 0x00);
		bytes[i] = TOUCH_RXDATA;
	}

	p1.x = bytes[0] + bytes[1];
	p1.y = bytes[2] + bytes[3];

	return p1;
}

