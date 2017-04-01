/*
 * order.h
 *
 * Created on: March 27, 2017
 *      Author: edolinsky
 */

#ifndef ORDER_H_
#define ORDER_H_

// Order functions.
char* read_order();
void get_order(char* customer_id, char* order_id);
char* read_restaurant_id();
void get_restaurant_id();
char* read_table_id();

#endif /* ORDER_H_ */
