/*
 * main.c
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include "serial/rs232.h"
#include "serial/bluetooth.h"
#include "serial/wifi.h"
#include "serial/touch.h"
#include "serial/graphics.h"
#include "serial/colours.h"
#include "apps/order.h"
#include "mainmenu.h"
#include "main.h"

int main(){
	app_context = 0;
	initTouch();
	printf("Hello from Nios II!\n");

	initWiFi();
	usleep(1500000);

	get_restaurant_id();
	usleep(1500000);

	char* restaurant_id = read_restaurant_id();
	printf(restaurant_id);

	drawMenu();
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

int sign(int num) {
	if (num < 0) {
		return -1;
	} else if (num > 0) {
		return 1;
	} else {
		return 0;
	}
}

