/*
 * order.h
 *
 * Created on: March 27, 2017
 *      Author: edoinsky
 */

#ifndef ORDER_H_
#define ORDER_H_

#include "../main.h"
#include "../serial/wifi.h"

// Order functions.
char* read_order();
void get_order(char* customer_id, char* order_id);

#endif /* ORDER_H_ */
