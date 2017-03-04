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

void bluetoothListen() {
	while (1){
		int x = getChar(&BT_STATUS, &BT_RXDATA);
		printf("%d",x);
	}
}
