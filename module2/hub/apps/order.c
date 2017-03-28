/*
 * order.c
 *
 * Created on: March 27, 2017
 *      Author: edoinsky
 */

#include "order.h"

char* read_restaurant_id() {
    putString(&WIFI_STATUS, &WIFI_TXDATA, "read_restaurant_id\n\n");
    int maxBuf = 100;
    char *buf = malloc(maxBuf);
    int i = 0;
    while (i < maxBuf) {
        char x = getChar(&WIFI_STATUS, &WIFI_RXDATA);
        if (x == '`')
            break;
        buf[i++] = x;
    }
    return buf;
}

/**
 * Issues a command to read the on-chip order file.
 */
char* read_order() {
    putString(&WIFI_STATUS, &WIFI_TXDATA, "read_file(ORDER_FILE)\n\n");
    int maxBuf = 1000;
    char *buf = malloc(maxBuf);
    int i = 0;
    while (i < maxBuf) {
        char x = getChar(&WIFI_STATUS, &WIFI_RXDATA);
        if (x == '`')
            break;
        buf[i++] = x;
    }
    return buf;
}

/**
 * Prepares and sends a command to get the user's order, provided two
 * null-terminated strings. This overwrites the on-chip order file.
 * Some delay should be taken before trying to read the file.
 */
void get_order(char* customer_id, char* order_id) {
    int maxBuf = 40;
    char[] cmd_name = "get_order(\0";   // 10 characters w/o \0.
    char[] delim = ", \0";              //  1 character  w/o \0.
    char[] closing = ")\n\n\0";         //  3 characters w/o \0.

    // customer ID and order ID are 10 characters each (not including null char).
    char *command = malloc(maxBuf);

    int cmd_idx = 0;
    int i = 0;

    // Read in first chunck.
    for (i = 0; cmd_name[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = cmd_name[i];
    }

    // Read in first paramter, customer_id.
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
    for (i = 0, closing[i] != '\0'; i++, cmd_idx++) {
        command[cmd_idx] = closing[i];
    }

    putString($WIFI_STATUS, &WIFI_TXDATA, command);
    free(command);
}
