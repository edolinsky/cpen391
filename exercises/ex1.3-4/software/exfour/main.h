/*
 * main.h
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#ifndef MAIN_H_
#define MAIN_H_

#define USLEEP_SEC 1000000

// all register addresses all begin with '8' to bypass data cache on NIOS

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

//#define GraphicsCommandReg (*(volatile unsigned short int *)(0x84000000))
//#define GraphicsStatusReg (*(volatile unsigned short int *)(0x84000000))
//#define GraphicsX1Reg (*(volatile unsigned short int *)(0x84000002))
//#define GraphicsY1Reg (*(volatile unsigned short int *)(0x84000004))
//#define GraphicsX2Reg (*(volatile unsigned short int *)(0x84000006))
//#define GraphicsY2Reg (*(volatile unsigned short int *)(0x84000008))
//#define GraphicsColourReg (*(volatile unsigned short int *)(0x8400000E))
//#define GraphicsBackGroundColourReg (*(volatile unsigned short int *)(0x84000010))

/************************************************************************************************
** This macro pauses until the graphics chip status register indicates that it is idle
***********************************************************************************************/
#define WAIT_FOR_GRAPHICS while((GraphicsStatusReg & 0x0001) != 0x0001);

// #defined constants representing values we write to the graphics 'command' register to get
// it to draw something. You will add more values as you add hardware to the graphics chip
// Note DrawHLine, DrawVLine and DrawLine at the moment do nothing - you will modify these
#define DrawHLine 1
#define DrawVLine 2
#define DrawLine 3
#define PutAPixel 0xA
#define GetAPixel 0xB
#define ProgramPaletteColour 0x10

// defined constants representing colours pre-programmed into colour palette
// there are 256 colours but only 8 are shown below, we write these to the colour registers
//
// the header files "Colours.h" contains constants for all 256 colours
// while the course file “ColourPaletteData.c” contains the 24 bit RGB data
// that is pre-programmed into the palette
#define BLACK 0
#define WHITE 1
#define RED 2
#define LIME 3
#define BLUE 4
#define YELLOW 5
#define CYAN 6
#define MAGENTA 7


#endif /* MAIN_H_ */
