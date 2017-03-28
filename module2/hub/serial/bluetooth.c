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
	setBluetoothName("Group15");
	setBluetoothPassword("cpen391");
	printf("Bluetooth initialized!\n");
	bluetoothListen();
}

void setBluetoothName(char *name) {
	// wait a second before and after entering command mode
	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "$$$"); // enter command mode
	usleep(USLEEP_SEC);

	// send the command to change the name
	char command[25] = "SN,";
	strcat(command, name);
	strcat(command, "\r\n");

	putString(&BT_STATUS, &BT_TXDATA, command);


	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "---"); // enter command mode
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
	strcat(command, "\r\n");

	putString(&BT_STATUS, &BT_TXDATA, command);


	usleep(USLEEP_SEC);
	putString(&BT_STATUS, &BT_TXDATA, "---"); // enter command mode
	usleep(USLEEP_SEC);
}

char* bluetoothListen() {
	int maxBuf = 40;
	int i = 0;
	char x = '\0';
	char* buffer = malloc(maxBuf);

	// Read character by character until gravemarker is hit.
	while (i = 0; x != '`' && i < maxBuf; i++){
		x = getChar(&BT_STATUS, &BT_RXDATA);
		buffer[i] = x;
	}

	// Replace final gravemarker (or final character) with null character
	buffer[i - 1] = '\0';
	return buffer;
}
