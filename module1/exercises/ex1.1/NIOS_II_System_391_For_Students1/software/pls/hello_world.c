/*
 * "Hello World" example.
 *
 * This example prints 'Hello from Nios II' to the STDOUT stream. It runs on
 * the Nios II 'standard', 'full_featured', 'fast', and 'low_cost' example
 * designs. It runs with or without the MicroC/OS-II RTOS and requires a STDOUT
 * device in your system's hardware.
 * The memory footprint of this hosted application is ~69 kbytes by default
 * using the standard reference design.
 *
 * For a reduced footprint version of this template, and an explanation of how
 * to reduce the memory footprint for a given application, see the
 * "small_hello_world" template.
 *
 */

#include <stdio.h>
#include "altera_up_avalon_character_lcd.h"
#define switches (volatile char *) 0x00002000
#define leds (char *) 0x00002010
#define pushbuttons (volatile char *) 0x00002060

int main()
{
	printf("Hello from Nios II!\n");
		alt_up_character_lcd_dev * char_lcd_dev;
		printf("%d \n", (*pushbuttons));


		// open the Character LCD port
		char_lcd_dev = alt_up_character_lcd_open_dev ("/dev/character_lcd_0");

		if ( char_lcd_dev == NULL)
			alt_printf ("Error: could not open character LCD device\n");
		else
			alt_printf ("Opened character LCD device\n");

		/* Initialize the character display */
		alt_up_character_lcd_init (char_lcd_dev);

		/* Write "Welcome to" in the first row */
		alt_up_character_lcd_string(char_lcd_dev, "Pushbuttons on:");

		/* Write "the DE2 board" in the second row */
		char second_row[] = "the DE2 board\0";

		alt_up_character_lcd_set_cursor_pos(char_lcd_dev, 0, 1);
		//alt_up_character_lcd_string(char_lcd_dev, second_row);
		while (1){
		       if(*pushbuttons == 7){ // first button
		    	   alt_up_character_lcd_set_cursor_pos(char_lcd_dev, 0, 1);
		    	   alt_up_character_lcd_string(char_lcd_dev, "3");
		       } else if(*pushbuttons == 11){ // second button
		    	   alt_up_character_lcd_set_cursor_pos(char_lcd_dev, 0, 1);
		    	   alt_up_character_lcd_string(char_lcd_dev, "2");
		       } else if(*pushbuttons == 13){ // third button
		    	   alt_up_character_lcd_set_cursor_pos(char_lcd_dev, 0, 1);
		    	   alt_up_character_lcd_string(char_lcd_dev, "1");
		       } else if(*pushbuttons == 14){
		    	   alt_up_character_lcd_set_cursor_pos(char_lcd_dev, 0, 1);
		    	   alt_up_character_lcd_string(char_lcd_dev, "0");
		       }
		}
		return 0;
}
