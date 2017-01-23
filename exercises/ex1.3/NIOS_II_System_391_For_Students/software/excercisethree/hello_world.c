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

/* a data type to hold a point/coord */
typedef struct {
	int x, y;
} Point;

// RS232 funtions
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

	Init_RS232();
	char chars[] = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };

	int i = 0;
	for (i = 0; i < 11; i++) {
		putcharRS232(chars[i]);
	}

	return 0;
}

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
	RS232_CONTROL = 0x15;
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

/*****************************************************************************
 ** Initialise touch screen controller
 *****************************************************************************/
void Init_Touch(void) {
	// Program 6850 and baud rate generator to communicate with touchscreen
	// send touchscreen controller an "enable touch" command
}

/*****************************************************************************
 ** test if screen touched
 *****************************************************************************/
int ScreenTouched(void) {
	// return TRUE if any data received from 6850 connected to touchscreen
	// or FALSE otherwise
}

/*****************************************************************************
 ** wait for screen to be touched
 *****************************************************************************/
void WaitForTouch() {
	while (!ScreenTouched())
		;
}

/*****************************************************************************
 * This function waits for a touch screen press event and returns X,Y coord
 *****************************************************************************/
Point GetPress(void) {
	Point p1;
	// wait for a pen down command then return the X,Y coord of the point
	// calibrated correctly so that it maps to a pixel on screen
	return p1;
}

/*****************************************************************************
 * This function waits for a touch screen release event and returns X,Y coord
 *****************************************************************************/
Point GetRelease(void) {
	Point p1;
	// wait for a pen up command then return the X,Y coord of the point
	// calibrated correctly so that it maps to a pixel on screen
	return p1;
}
