/** yotkaz
 *
 *  voltage on LCD (RGB: changing backlight)
 *  Arduino UNO + LCD 16x2 (RGB or not) */

#include <LiquidCrystal.h>

#define P_RS 8
#define P_ENABLE 7
#define P_D4 5
#define P_D5 4
#define P_D6 3
#define P_D7 2

#define A_INPUT A0

/* rgb only */
#define RED_PIN 9
#define GREEN_PIN 10
#define BLUE_PIN 11

const float   VOLTAGE_RANGE[] = {0.0, 5.0};
const int     LCD_SIZE[]      = {16, 2};
const int     START_COLUMN    = 0;
const int     START_ROW       = 0;
const int     AC_RANGE        = 1024;
const int     DECIMAL_PLACES  = 3;
const long    DELAY_TIME      = 500;

/* rgb only */
const bool    RGB                   = true;
const bool    INVERT_COLORS         = true;
const int     BRIGHTNESS            = 255;
const int     START_COLOR[]         = {255, 200, 20};

LiquidCrystal lcd(P_RS, P_ENABLE, P_D4, P_D5, P_D6, P_D7);

void setup() {
  lcd.begin(LCD_SIZE[0], LCD_SIZE[1]);
  if (RGB) {
    pinMode(RED_PIN, OUTPUT);
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(BLUE_PIN, OUTPUT);
    setBacklight(START_COLOR[0], START_COLOR[1], START_COLOR[2]);
  }
}

void loop() {
  lcd.clear();
  int inputValue = analogRead(A_INPUT);
  float voltage = VOLTAGE_RANGE[0] + inputValue * (VOLTAGE_RANGE[1] / (AC_RANGE - 1.0));
  lcd.setCursor(START_COLUMN, START_ROW);
  if (RGB) {
    mapBacklight(voltage);
  }
  lcd.print(String(voltage, DECIMAL_PLACES) + "V");
  delay(DELAY_TIME);
}

/* green (low) to red (high) */
void mapBacklight(float voltage) {
  setBacklight(255, map(voltage * 100, VOLTAGE_RANGE[0] * 100, VOLTAGE_RANGE[1] * 100, 255, 0), 0); 
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
