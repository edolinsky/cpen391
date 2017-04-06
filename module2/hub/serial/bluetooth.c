/*
 * bluetooth.c
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "bluetooth.h"
#include "../main.h"

/*****************************************************************************
 ** Initialise the bluetooth controller
 *****************************************************************************/
void initBluetooth(void) {

	// setting up the 6850
	BT_CONTROL = 0x95;	// 8 bits, no parity, 1 stop bit
	BT_BAUD = 0x01;	// 115k baud

	setBluetoothName("RESTY-1");
	printf("Bluetooth initialized!\n");
}

void setBluetoothName(char *name) {
	// wait a second before and after entering command mode
	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "$$$"); // enter command mode
	usleep(USLEEP_SEC);

	// send the command to change the name
	char command[25] = "SN,";
	strcat(command, name);
	strcat(command, "\n");

	putString(&BT_STATUS, &BT_TXDATA, command);

	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "---\n"); // enter command mode
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
	strcat(command, "\n");

	putString(&BT_STATUS, &BT_TXDATA, command);


	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "---\n"); // enter command mode
	usleep(USLEEP_SEC);
}

/**
 * Wait for a message to be received on the bluetooth chip. A message is padded
 * by any number of '`' characters before and after the message.
 */
char* bluetoothListen(char* buf, int maxLen) {
	int i = 1;

	while (getChar(&BT_STATUS, &BT_RXDATA) != '`') {
		// do nothing until gravemarker is hit.
	}

	char x;

	// Ignore successive gravemarkers.
	do {
		x = getChar(&BT_STATUS, &BT_RXDATA);
		buf[0] = x;
	} while (x == '`');

	// Read in message content.
	while (i < maxLen) {
		char x = getChar(&BT_STATUS, &BT_RXDATA);
		if (x == '`') {
			break;
		}
		buf[i++] = x;
	}

	buf[i] = '\0';
	return buf;
}
