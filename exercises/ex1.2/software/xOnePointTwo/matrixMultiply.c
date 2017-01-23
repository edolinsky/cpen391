#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "sys/alt_timestamp.h"
#include "sys/alt_alarm.h"

#define ROWS 100	// number of rows in matrix
#define COLS 100	// number of columns in matrix

// function definitions
void init_matrices(int a[][COLS], int b[][COLS], int c[][COLS]);
void multiply(int m1[][COLS], int m2[][COLS], int prod[][COLS]);

/**
 * Sets each entry of first matrix to be the index of its row,
 * the each entry of the second matrix to be the index of its column,
 * and the third matrix to consist of all 0s.
 */
void init_matrices(int a[][COLS], int b[][COLS], int c[][COLS]) {
	int i, j;

	// set element in each of the three matrices
	for(i = 0; i < COLS; i++) {
		for(j = 0; j < ROWS; j++) {
			a[i][j] = i;
			b[i][j] = j;
			c[i][j] = 0;
		}
	}
}

/**
 * Multiplies a two square matrices, and stores the output in a third
 * @param m1 first matrix, to multiply
 * @param m2 second matrix, to multiply
 * @param prod product of m1 * m2
 */
void multiply(int m1[][COLS], int m2[][COLS], int prod[][COLS]) {
	int i, j, k;
	int sum;

	// make sure that we have a square matrix
	assert(ROWS == COLS);

	// compute product
	for(i = 0; i < COLS; i++) {
		for(j = 0; j < ROWS; j++) {
			sum = 0;

			for(k = 0; k < ROWS; k++) {
				sum += m1[i][k] * m2[k][j];
			}

			prod[i][j] = sum;
		}
	}
}


int main(void) {

	// matrices
	int a [ROWS][COLS];
	int b [ROWS][COLS];
	int c [ROWS][COLS];

	  //////////////////////
	 /** TIMESTAMP TEST **/
	//////////////////////

	unsigned long start_time, end_time;	// timer values

	init_matrices(a, b, c);		// initialize matrices

	alt_timestamp_start();		// start timer

	// start timestamp timer
	start_time = (int) alt_timestamp();

	// calculate
	multiply(a, b, c);

	// stop timer
	end_time = (int) alt_timestamp();

	printf("Timestamp Time Taken: %lu clock ticks\n", end_time - start_time);
	printf("                      %f seconds\n", (float)(end_time - start_time) / (float)alt_timestamp_freq());


	return 0;
}
