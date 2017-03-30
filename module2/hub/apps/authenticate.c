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

#define RAND_MAX 9000
#define RAND_OFFSET 1000
#define DEVICE_ID "0xDEFEC7EDDA7ABA5E"

/**
 * Listens to bluetooth for a string, and compares it to the specified string.
 * Returns true if strings match.
 */
 int listen_for_pin_and_check(char* hub_pin) {
     char* pin_entered = bluetoothListen();

     // Return true only if strings are same (strcmp 0)
     int is_valid = strcmp(hub_pin, pin_entered) ? 0 : 1;
     free(pin_entered);

     return is_valid;
 }

/**
 * Generates a random 4-digit number string.
 */
 char* generate_random_pin() {
     int maxBuf = 40;
     char* buffer = malloc(maxBuf);

     // Generate random integer.
     srand(time(NULL));

     int pin = (rand() % RAND_MAX) + RAND_OFFSET;

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
 void send_table_info(char* restaurant_id) {
     // TODO
     return;
 }

 /**
  * Listens for an order ID over bluetooth.
  */
char* listen_for_order_id() {
    // TODO
    return;
}
