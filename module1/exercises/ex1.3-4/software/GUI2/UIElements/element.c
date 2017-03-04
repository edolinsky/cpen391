/*
 * element.c
 *
 *  Created on: Feb 2, 2017
 *      Author: omar
 */

#include "element.h"
#include "../serial/graphics.h"

// private functions
void drawText(Element *e);
void drawMe(Element *e);
void destroyElement(Element *e);

Element* createElement(int x, int y, int width, int height, int colour) {
	Element *e = malloc(sizeof(Element));
	e->x = x;
	e->y = y;
	e->width = width;
	e->height = height;
	e->elementColour = colour;
	e->text = strdup("");
	e->textSize = TEXT_LARGE;
	e->textColour = BLACK;
	return e;
}

void setElementText(Element *e, char *title) {
	free(e->text);
	e->text = strdup(title);
}

void setElementAction(Element *e, void *action) {
	e->action = action;
}

void drawMe(Element *e) {
	filledRectangleWithBorder(e->x, e->y, e->x + e->width, e->y + e->height,
			e->elementColour, BLACK);

	drawText(e);
}

void drawText(Element *e) {
    char* text = e->text;
    int textWidth, textHeight;
    if (e->textSize == TEXT_SMALL) {
        textWidth = 5;
        textHeight = 7;
    } else {
        textWidth = 10;
        textHeight = 14;
    }
    int lineLength = e->width / textWidth; // rounded down is what we want
    int maxLines = e->height / textHeight;
    char line[lineLength];
    int y = e->y;
    // vertical bounds
    int pos = 0;
    //int isNewWord = 1;
    int lineCount = 0;
    while (y < e->y + e->height - textHeight) {
        // create a line
        int i, length = 0;
        for (i = 0; i < lineLength; i++) {
            if (text[pos] == '\0') { // we are done
                break;
            }
            line[i] = text[pos++];
            length++;
        }
        // print line
        lineCount++;
        if (textWidth == FONT1_WIDTH) {
            // if not a full line then center text horizontally, also center it vertically if its the only line and we're done
            if (lineCount == 1 && length != lineLength) {
                writeString5x7(e->x + (textWidth * (lineLength - length) / 2),
                        y + (textHeight * (maxLines - lineCount) / 2), line, length, e->textColour, e->elementColour);
            } else {
                writeString5x7(e->x + (textWidth * (lineLength - length) / 2),
                        y, line, length, e->textColour, e->elementColour);
            }
        } else {
            if (lineCount == 1 && length != lineLength) {
                writeString10x14(e->x + (textWidth * (lineLength - length) / 2),
                        y + (textHeight * (maxLines - lineCount) / 2) , line, length, e->textColour, e->elementColour);
            } else {
                writeString10x14(e->x + (textWidth * (lineLength - length) / 2),
                        y, line, length, e->textColour, e->elementColour);
            }
        }
        // if length isn't lineLength then we finished, otherwise wrap text
        if (length != lineLength)
            break;
        y += textHeight;
    }
}

void refresh() {
	ElementNode *node = globalElementList->head;
	while (node != NULL) {
		ElementNode *next = node->next;
		drawMe(node->e);
		node = next;
	}
}

void destroyElement(Element *e) {
	free(e);
}

/**
 * Add an element to the global linked list of the elements
 */
void addElementToList(Element *e) {
	ElementNode *newNode = malloc(sizeof(ElementNode));
	newNode->e = e;
	newNode->next = NULL;
	if (globalElementList->tail == NULL) {
		globalElementList->head = newNode;
		globalElementList->tail = newNode;
	} else {
		globalElementList->tail->next = newNode;
		globalElementList->tail = newNode;
	}
}

void removeElementFromList(Element *e) {
	ElementNode *currP, *prevP;
	prevP = NULL;

	for (currP = globalElementList->head; currP != NULL; prevP = currP, currP =
			currP->next) {
		if (currP->e == e) { /* Found it. */
			if (prevP == NULL) {
				/* Fix beginning pointer. */
				globalElementList->head = currP->next;
			} else {
				prevP->next = currP->next;
			}
			destroyElement(currP->e);
			free(currP);
			return;
		}
	}
}

/**
 * need to run this function before creating any elements
 */
void initElements() {
	globalElementList = malloc(sizeof(ElementList));
	globalElementList->head = NULL;
	globalElementList->tail = NULL;
}

/**
 * need to call this function before exiting
 */
void elementsCleanup() {
    ElementNode *node = globalElementList->head;
    while (node != NULL) {
        ElementNode *next = node->next;
        free(node->e);
        free(node);
        node = next;
    }
}

void listenToTouches() {

	Point p;
	listen: getPress();
	p = getRelease();
	ElementNode *node = globalElementList->head;

	while (node != NULL) {
		Element *e = node->e;
		// check if the touch is within this elements bounds
		if (p.x > e->x && p.x < (e->x + e->width) && p.y > e->y
				&& p.y < (e->y + e->height)) {
			if (e->hasArg) {
				if (e->arg1 == BREAK_KEY) return;
				(*(e->action))(e->arg1);
			} else {
				(*(e->action))();
			}
			break;
		}
		node = node->next;
	}

	goto listen;
}

