/*
 * main.h
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#ifndef MAIN_H_
#define MAIN_H_

#define USLEEP_SEC 1000000

#define TRUE 1
#define FALSE 0

#define ORDER_CONTEXT 1
#define SUCCESS_CONTEXT 2

int app_context;
char* restaurant_id;
char* hub_id;
char* order_id;
char* customer_id;

// General purpose functions
int getChar(volatile unsigned char* status, volatile unsigned char* rx);
int putChar(volatile unsigned char* status, volatile unsigned char* tx, int c);
// sends a string to the serial tx, string must be terminated by \0, \0 is not sent
void putString(volatile unsigned char* status, volatile unsigned char* tx,
		char *string);
int sign(int num);

#endif /* MAIN_H_ */