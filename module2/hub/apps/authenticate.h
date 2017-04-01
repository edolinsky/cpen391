/*
 * authenticate.h
 *
 *  Created on: Mar 28, 2017
 *      Author: edolinsky
 */

 #ifndef AUTHENTICATE_H_
 #define AUTHENTICATE_H_

// Authenticate functions.
int listen_for_pin_and_check(char* hub_pin);
char* generate_random_pin(int seed);
void authenticate(char* pin);
void send_table_info();
void listen_for_order_info();
void get_time();
int read_time();
void send_auth_error();

 #endif /* AUTHENTICATE_H_ */
