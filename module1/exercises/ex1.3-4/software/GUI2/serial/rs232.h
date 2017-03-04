/*
 * rs232.h
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#ifndef RS232_H_
#define RS232_H_

#define RS232_CONTROL 	(*(volatile unsigned char *) (0x84000200))
#define RS232_STATUS 	(*(volatile unsigned char *) (0x84000200))
#define RS232_TXDATA 	(*(volatile unsigned char *) (0x84000202))
#define RS232_RXDATA 	(*(volatile unsigned char *) (0x84000202))
#define RS232_BAUD 		(*(volatile unsigned char *) (0x84000204))

#endif /* RS232_H_ */
