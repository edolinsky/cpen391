/*
 * graphics.c
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#include <stdio.h>
#include <stdlib.h>
#include "graphics.h"
#include "../main.h"

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
	GraphicsX1Reg = x; // write coords to x1, y1
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
	WAIT_FOR_GRAPHICS;
	GraphicsX1Reg = x1;
	GraphicsY1Reg = y1;
	GraphicsX2Reg = x1 + length - 1;
	GraphicsColourReg = Colour;
	GraphicsCommandReg = DrawHLine;
}

/*********************************************************************************************
This function draw a vertical line, 1 pixel at a time starting at the x,y coords specified
*********************************************************************************************/
void VLine(int x1, int y1, int length, int Colour) {
	WAIT_FOR_GRAPHICS;
	GraphicsX1Reg = x1;
	GraphicsY1Reg = y1;
	GraphicsY2Reg = y1 + length - 1;
	GraphicsColourReg = Colour;
	GraphicsCommandReg = DrawVLine;
}

/*******************************************************************************
** Implementation of Bresenhams line drawing algorithm
*******************************************************************************/
void Line(int x1, int y1, int x2, int y2, int Colour) {
	WAIT_FOR_GRAPHICS;
	GraphicsX1Reg = x1;
	GraphicsY1Reg = y1;
	GraphicsX2Reg = x2;
	GraphicsY2Reg = y2;
	GraphicsColourReg = Colour;
	GraphicsCommandReg = DrawLine;
 }

// shapes and routines

void screenFill(int colour) {
	int i;
	for (i = 0; i < YRES; i++) {
		HLine(0, i, XRES, colour);
	}
}

void rectangle(int x1, int y1, int x2, int y2, int colour) {
	HLine(x1, y1, x2-x1, colour);
	HLine(x1, y2, x2-x1, colour);
	VLine(x1, y1, y2-y1, colour);
	VLine(x2, y1, y2-y1, colour);
}

void filledRectangle(int x1, int y1, int x2, int y2, int colour) {
	int i;
	for(i = 0; i < y2 - y1; i++) {
		HLine(x1, y1 + i, x2-x1, colour);
	}
}

void filledRectangleWithBorder(int x1, int y1, int x2, int y2, int colour,
		int bordercolour) {

	filledRectangle(x1, y1, x2, y2, colour);
	rectangle(x1, y1, x2, y2, bordercolour);
}

void triangle(int x1, int y1, int x2, int y2, int x3, int y3, int colour) {
	Line(x1, y1, x2, y2, colour);
	Line(x1, y1, x3, y3, colour);
	Line(x2, y2, x3, y3, colour);
}

void writeString5x7(int x, int y, char* string, int length, int colour, int bg,
		int erase) {

	int i;
	for(i = 0; i < length; i++) {
		OutGraphicsCharFont1(x + (FONT1_WIDTH*i), y, colour, bg, string[i], erase);
	}
}

void writeString10x14(int x, int y, char* string, int length, int colour, int bg,
		int erase) {

	int i;
	for(i = 0; i < length; i++) {
		OutGraphicsCharFont2a(x + (FONT2_WIDTH*i), y, colour, bg, string[i], erase);
	}
}

void graphicsDemo() {
	filledRectangleWithBorder(100, 100, 600, 400, CYAN, RED);
	rectangle(0, 0, 200, 200, BLACK);
	filledRectangle(500, 400, 700, 450, BLACK);
	triangle(200, 0, 300, 100, 100, 100, BLACK);

	Line(0, 0, 800, 100, BLUE);
	Line(0, 0, 800, 200, RED);
	Line(0, 0, 800, 300, BLUE);
	Line(0, 0, 800, 400, RED);
	Line(0, 0, 800, 480, BLUE);
}
