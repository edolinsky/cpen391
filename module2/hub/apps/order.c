/*
 * order.c
 *
 * Created on: March 27, 2017
 *      Author: edolinsky
 */

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "order.h"
#include "../main.h"
#include "../serial/rs232.h"
#include "../serial/wifi.h"

/**
 * Command WiFi chip to retrieve the restaurant ID for this device from the API.
 */
void get_restaurant_id() {
	putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\n\r\nget_restaurant_id()\r\n");
}

/**
 * Read the restaurant ID stored on the WiFi chip.
 */
char* read_restaurant_id() {
	int maxBuf = 100;
	char *buf = malloc(maxBuf);

	putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\nread_restaurant_id()\r\n");
    return wifiListen(buf, maxBuf);
}

/**
 * Read this device's unique ID from the WiFi chip.
 */
char* read_table_id() {
	int maxBuf = 20;
	char *buf = malloc(maxBuf);

	putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\nread_table_id()\r\n");
	return wifiListen(buf, maxBuf);
}

/**
 * Issues a command to read the on-chip order file.
 */
char* read_order() {
	int maxBuf = 1000;
	char *buf = malloc(maxBuf);
    putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\n\r\nread_file(ORDER_FILE)\r\n");

    return wifiListen(buf, maxBuf);
}

/**
 * Prepares and sends a command to get the user's order, provided two
 * null-terminated strings. This overwrites the on-chip order file.
 * Some delay should be taken before trying to read the file.
 */
void get_order(char* customer_id, char* order_id) {
    int maxBuf = 50;
    char cmd_name[] = "\r\nget_order(\"\0";   // 11 characters w/o \0.
    char delim[] = "\", \"\0";            //  4 characters w/o \0.
    char closing[] = "\")\r\n\0";         //  4 characters w/o \0.

    // customer ID and order ID are 10 characters each (not including null char).
    char *command = malloc(maxBuf);

    int cmd_idx = 0;
    int i = 0;

    // Read in first chunk.
    for (i = 0; cmd_name[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = cmd_name[i];
    }

    // Read in first parameter, customer_id.
    for (i = 0; customer_id[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = customer_id[i];
    }

    // Read in parameter delimiter.
    for (i = 0; delim[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = delim[i];
    }

    // Read in second parameter, order_id.
    for (i = 0; order_id[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = order_id[i];
    }

    // Close off command.
    for (i = 0; closing[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = closing[i];
    }

    putString(&WIFI_STATUS, &WIFI_TXDATA, command);
    free(command);
}
