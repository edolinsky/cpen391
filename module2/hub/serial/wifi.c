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

void wifiListen() {
	
}
