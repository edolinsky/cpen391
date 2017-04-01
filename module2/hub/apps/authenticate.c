/*
 * authenticate.c
 *
 *  Created on: Mar 28, 2017
 *      Author: edolinsky
 */

#include "../serial/bluetooth.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include "../serial/wifi.h"
#include "../main.h"

#define RANDOM_MAX 9000
#define RAND_OFFSET 1000

char MSG_START[] = "````````````````````";
char MSG_END[] = "`";

/**
 * Listens to bluetooth for a string, and compares it to the specified string.
 * Returns true if strings match.
 */
int listen_for_pin_and_check(char* hub_pin) {
	int maxBuf = 10;
	char* pin_entered = malloc(maxBuf);
	pin_entered = bluetoothListen(pin_entered, maxBuf);

	printf("Pin entered: ");
	printf(pin_entered);
	printf("\n");

    // Return true only if strings are same (strcmp 0)
    int is_valid = strcmp(hub_pin, pin_entered) ? 0 : 1;
    free(pin_entered);

    return is_valid;
}

/**
 * Generates a random 4-digit number string.
 */
char* generate_random_pin(int seed) {
    int maxBuf = 40;
    char* buffer = malloc(maxBuf);

    // Generate random integer.
    srand(seed);

    int pin = (rand() % RANDOM_MAX) + RAND_OFFSET;

    // Convert integer to string.
    snprintf(buffer, maxBuf, "%d", pin);

    return buffer;
}

/**
 * Hangs until a valid pin (as specified by the input string) is
 * received via bluetooth.
 */
void authenticate(char* pin) {
	int authenticated = 0;

    while(!authenticated) {
        if (listen_for_pin_and_check(pin)) {
            authenticated = 1;
        }
    }
}

/**
 * Sends this device's device (table) ID and restaurant ID via bluetooth.
 */
void send_table_info() {
	char message[80] = "";
	strcat(message, MSG_START);
	strcat(message, hub_id);
	strcat(message, "    ");
	strcat(message, restaurant_id);
	strcat(message, MSG_END);

    putString(&BT_STATUS, &BT_TXDATA, message);
}

void send_auth_error() {
	char message[80] = "";
	strcat(message, MSG_START);
	strcat(message, "error");
	strcat(message, MSG_END);
	putString(&BT_STATUS, &BT_TXDATA, message);
}

 /**
  * Listens for an order ID over bluetooth.
  */
void listen_for_order_info() {
	int maxBuf = 20;
	char *info = malloc(maxBuf);
	info = bluetoothListen(info, maxBuf);

	char order_buf[20];
	char customer_buf[20];

	int i = 0;
	int order_id_len;
	int customer_id_len;
	// parse info. "order_id,customer_id"
	for (i = 0; i < maxBuf && info[i] != ','; i++) {
		order_buf[i] = info[i];
	}
	order_id_len = i;	// with one extra for null character.
	order_buf[i - 1] = '\0';

	int j;
	for (j = 0; i < maxBuf && info[i] != '\0'; i++, j++) {
		customer_buf[j] = info[i];
	}
	customer_id_len = j;
	customer_buf[j - 1] = '\0';

	order_id = malloc(order_id_len);
	customer_id = malloc(customer_id_len);

	strcpy(order_id, order_buf);
	strcpy(customer_id, customer_buf);
}

/**
 * Instruct the wifi chip to retrieve the current time.
 */
void get_time() {
	putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\n\r\nget_time()\r\n");
}

/**
 * Read the most recently stored timestamp on the wifi chip.
 */
int read_time() {
	int maxBuf = 1000;
	char *buf = malloc(maxBuf);
	putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\n\r\nread_file(TIME_FILE)\r\n");

	buf = wifiListen(buf, maxBuf);

	int time = atoi(buf);
	free(buf);

	return time;
}
