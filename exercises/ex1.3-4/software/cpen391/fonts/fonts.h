/*
 * fonts.h
 *
 *  Created on: Feb 2, 2017
 *      Author: reidoliveira
 */

#ifndef FONTS_H_
#define FONTS_H_

extern const char * const Font5x7[95][7];
extern const char * const Font10x14[][14];

void OutGraphicsCharFont1(int x, int y, int fontcolour, int backgroundcolour, int c, int Erase);
void OutGraphicsCharFont2a(int x, int y, int colour, int backgroundcolour, int c, int Erase);

#endif /* FONTS_H_ */
