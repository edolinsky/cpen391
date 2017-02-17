/*
 * wifi.c
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "wifi.h"
#include "../main.h"

/*****************************************************************************
 ** Initialise the wifi controller
 *****************************************************************************/
void initWiFi(void) {

	// setting up the 6850
	WIFI_CONTROL = 0x95;	// 8 bits, no parity, 1 stop bit
	WIFI_BAUD = 0x01;	// 115k baud

//	putChar(&WIFI_STATUS, &WIFI_TXDATA,"\n");
//	usleep(300);
//	putChar(&WIFI_STATUS, &WIFI_TXDATA,"\n");
//	usleep(300);
//	putChar(&WIFI_STATUS, &WIFI_TXDATA,"\n");
//	usleep(300);
//	putChar(&WIFI_STATUS, &WIFI_TXDATA,"\n");
//	usleep(300);

	printf("WiFi initialized!\n");
}

void setWiFiName(char *name) {
	// wait a second before and after entering command mode
	usleep(USLEEP_SEC);
	putString(&WIFI_STATUS, &WIFI_TXDATA, "$$$"); // enter command mode
	usleep(USLEEP_SEC);

	// send the command to change the name
	char command[25] = "SN,";
	strcat(command, name);
	strcat(command, "\r\n");

	putString(&WIFI_STATUS, &WIFI_TXDATA, command);

	usleep(USLEEP_SEC);
	putString(&WIFI_STATUS, &WIFI_TXDATA, "---"); // enter command mode
	usleep(USLEEP_SEC);
}

void setWiFiPassword(char *password) {
	// wait a second before and after entering command mode
	usleep(USLEEP_SEC);
	putString(&WIFI_STATUS, &WIFI_TXDATA, "$$$"); // enter command mode
	usleep(USLEEP_SEC);

	// send the command to change the name
	char command[25] = "SP,";
	strcat(command, password);
	strcat(command, "\r\n");

	putString(&WIFI_STATUS, &WIFI_TXDATA, command);

	usleep(USLEEP_SEC);
	putString(&WIFI_STATUS, &WIFI_TXDATA, "---"); // enter command mode
	usleep(USLEEP_SEC);
}

void wifiListen() {

}
