/** yotkaz
 *
 *  print text on LCD and send back ASCII codes via serial
 *  Arduino UNO + LCD 16x2 (RGB or not) */

#include <LiquidCrystal.h>

#define SERIAL_BAUD 9600

#define P_RS 8
#define P_ENABLE 7
#define P_D4 5
#define P_D5 4
#define P_D6 3
#define P_D7 2

/* rgb only */
#define RED_PIN 9
#define GREEN_PIN 10
#define BLUE_PIN 11

const String  HELLO_MESSAGE_SERIAL  = "Hello! Type your text and press ENTER:";
const String  HELLO_MESSAGE_LCD     = "Hello! Type text via Serial :)";

const char    LINE_ENDING           = '\n';
const char    ASCII_SEPERATOR       = ' ';
const char    WHITECHARS_END        = ' ';
const int     LCD_SIZE[]            = {16, 2};
const int     START_POSITION        = 0;
const int     START_ROW             = 0;
const bool    BLINK                 = false;

/* rgb only */
const bool    RGB                   = true;
const bool    INVERT_COLORS         = true;
const int     BRIGHTNESS            = 255;
const int     START_COLOR[]         = {255, 200, 20};
const bool    RANDOM_COLOR          = false;

LiquidCrystal lcd(P_RS, P_ENABLE, P_D4, P_D5, P_D6, P_D7);

void setup() {
  lcd.begin(LCD_SIZE[0], LCD_SIZE[1]);
  Serial.begin(SERIAL_BAUD);
  Serial.println(HELLO_MESSAGE_SERIAL);
  if (RGB) {
    pinMode(RED_PIN, OUTPUT);
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(BLUE_PIN, OUTPUT);
    setBacklight(START_COLOR[0], START_COLOR[1], START_COLOR[2]);
  }
  if (BLINK) {
    lcd.blink(); 
  }
  printText(HELLO_MESSAGE_LCD);
}

void loop() {
  String input;
  while (!Serial.available()) {
    // wait
  }
  if (RGB && RANDOM_COLOR) {
    setRandomBacklight();
  }
  input = Serial.readStringUntil(LINE_ENDING);
  printText(input);
  Serial.println(toASCII(input));
}

String toASCII(String input) {
  String result = "";
  int lastCharIndex = input.length() - 1;
  for (int i = 0; i < lastCharIndex; i++) {
    result = String(result + (int) input.charAt(i) + ASCII_SEPERATOR);
  }
  result = String(result + (int) input.charAt(lastCharIndex));
  return result;
}

/* print (wrapping longer texts to next lines) */
void printText(String text) {
  lcd.clear();
  int skippedChars = 0;
  for (int y = START_ROW; y < LCD_SIZE[1]; y++) {
    /* avoid empty space on the beginning of line */
    int charIndex = y * LCD_SIZE[0] + skippedChars;
    while (text.charAt(charIndex) <= WHITECHARS_END) {
      skippedChars++;
      charIndex = y * LCD_SIZE[0] + skippedChars;
      if (charIndex >= text.length()) {
        return;
      }
    }
    for (int x = START_POSITION; x < LCD_SIZE[0]; x++) {
      int charIndex = y * LCD_SIZE[0] + x + skippedChars;
      if (charIndex >= text.length()) {
        return;
      }
      lcd.setCursor(x, y);
      lcd.print(text.charAt(charIndex));
    }
  }
}

/* rgb only */
void setRandomBacklight() {
  int r = random(0, 255);
  int g = random(0, 255);
  int b = random(0, 255);
  setBacklight(r, g, b);
}

/* rgb only */
void setBacklight(int r, int g, int b) {
  /* map to brightness */
  r = map(r, 0, 255, 0, BRIGHTNESS);
  g = map(g, 0, 255, 0, BRIGHTNESS);
  b = map(b, 0, 255, 0, BRIGHTNESS);

  /* invert colors*/
  if (INVERT_COLORS) {
    r = map(r, 0, 255, 255, 0);
    g = map(g, 0, 255, 255, 0);
    b = map(b, 0, 255, 255, 0); 
  }
  
  analogWrite(RED_PIN, r);
  analogWrite(GREEN_PIN, g);
  analogWrite(BLUE_PIN, b);
}
