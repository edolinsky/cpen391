/*
 * touch.h
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#ifndef TOUCH_H_
#define TOUCH_H_

#define TOUCH_CONTROL 	(*(volatile unsigned char *) (0x84000230))
#define TOUCH_STATUS 	(*(volatile unsigned char *) (0x84000230))
#define TOUCH_TXDATA 	(*(volatile unsigned char *) (0x84000232))
#define TOUCH_RXDATA 	(*(volatile unsigned char *) (0x84000232))
#define TOUCH_BAUD 		(*(volatile unsigned char *) (0x84000234))

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

#endif /* TOUCH_H_ */
