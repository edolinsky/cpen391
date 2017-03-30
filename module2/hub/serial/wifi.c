/*
 * wifi.c
 *
 *  Created on: Mar 27, 2017
 *      Author: edolinsky
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "wifi.h"
#include "../main.h"

/**
 * Initialize WiFi controller
 */
void initWiFi(void) {

	// setting up the 6850
	WIFI_CONTROL = 0x95;	// 8 bits, no parity, 1 stop bit
	WIFI_BAUD = 0x01;	// 115k baud

	printf("WiFi initialized!\n");
}

char* wifiListen(char* buf, int maxLen) {
	int i = 1;
	
	while (getChar(&WIFI_STATUS, &WIFI_RXDATA) != '`') {
		// do nothing until gravemarker is hit.
	}

	char x;

	// Ignore successive gravemarkers.
	do {
		x = getChar(&WIFI_STATUS, &WIFI_RXDATA);
		buf[0] = x;
	} while (x == '`');

	// Read in message content.
	while (i < maxLen) {
		char x = getChar(&WIFI_STATUS, &WIFI_RXDATA);
		if (x == '`') {
			break;
		}
		buf[i++] = x;
	}

	buf[i] = '\0';
	return buf;
}
