
public class Algebric {

//Method: return Postfix String of equation
	public String convertTOPostfix(String str) {
		int charpos = 0;
		boolean isPrevDigit = true;// true if the previous char is digit , false if is not a digit
		char nextCharacter;
		char topOperator; // top of stack
		String postfixReslt = "";
		ArrayStack<Character> operatorStack = new ArrayStack<Character>(str.length());
		while (charpos < str.length()) {
			nextCharacter = str.charAt(charpos);
			switch (nextCharacter) {
			case '(':
			case '[':
			case '^':
				operatorStack.push(nextCharacter);

				break;
			case '*':
			case '/':
			case '+':
			case '-':
				isPrevDigit = false;
				while (!operatorStack.isEmpty()
						&& findPrecedence(nextCharacter) <= findPrecedence((char) operatorStack.peek())) {
					// case precedence of char in input string <= precedence of top of stack
					topOperator = (char) operatorStack.pop();
					postfixReslt += " " + topOperator;

				}
				// case precedence of char in input string > precedence of top of stack
				operatorStack.push(nextCharacter);
				break;
			case ')':
				isPrevDigit = false;
				topOperator = (char) operatorStack.pop();
				while (topOperator != '(') {
					// adding the contents of stack to postfixReslt till reaching the opening (
					postfixReslt += " " + topOperator;
					topOperator = (char) operatorStack.pop();
				}
				break;

			case ']':
				isPrevDigit = false;
				topOperator = (char) operatorStack.pop();
				while (topOperator != '[') {
					// adding the contents of stack to postfixReslt till reaching the opening [
					postfixReslt += " " + topOperator;
					topOperator = (char) operatorStack.pop();
				}
				break;
			case 32:
				break;
			default:// for number
				if (isPrevDigit) // the number has more than 1 digit
					postfixReslt += nextCharacter;

				else
					postfixReslt += " " + nextCharacter;
				isPrevDigit = true;
				break;

			}

			charpos++;
		}
		// end of while all contents of stack moved to postfixReslt
		while (!operatorStack.isEmpty()) {
			topOperator = (char) operatorStack.pop();
			postfixReslt += " " + topOperator;

		}
		return postfixReslt;
	}

//Method : return double ( evaluation of Postfix String) 
	public double evaluatePostfix(String postfix) {
		String[] Token = postfix.split(" ");// split string by space

		double result;
		ArrayStack<Double> valueStack = new ArrayStack<>(postfix.length());// stack for value
		for (int i = 0; i < Token.length; i++) {// loop for the array of string

			switch (Token[i].charAt(0)) {

			case '*':
			case '/':
			case '+':
			case '-':
			case '^':
				if (!valueStack.isEmpty()&&valueStack.getN()>=1) {// if the stack is not empty
					result = eval((double) valueStack.pop(), (double) valueStack.pop(), Token[i].charAt(0));
					valueStack.push(result);
				}

				break;

			default:
				valueStack.push(Double.parseDouble(Token[i].trim()));
				break;
			}
		}
		return (double) valueStack.pop();
	}

// Method : return int precedence of operation 
	private int findPrecedence(char ch) {
		switch (ch) {
		case '+':
		case '-':
			return 1;

		case '*':
		case '/':
			return 2;

		case '^':
			return 3;
		}
		return -1;
	}

// Method return true when equation is valid and false for not vatid	
	public boolean isBalance(String infix) {
		// isback true when previous char is a digit , false non-digit
		// isfront true when next char is a digit , false non-digit
		boolean isback = false, isfront = false;
		// hasPreOpera : previous char is operator --> true
		// hasPreOpera : next char is Operator --- > true
		boolean hasPreOpera = false, hasNexOpera = false;
		boolean isBalance = true;
		char nextCharacter;
		int charpos = 0;
		ArrayStack<Character> operandStack = new ArrayStack<>(infix.length());
		while (isBalance == true && charpos < infix.length()) {
			nextCharacter = infix.charAt(charpos);
			switch (nextCharacter) {
			case '(':
			case '[':
				hasPreOpera = false;
				hasNexOpera = false;
				isback = false;
				isfront = false;
				if (charpos != 0) // not the first char
					// case ( or [ and the previous is digit
					if (Character.isDigit(infix.charAt(charpos - 1))) {
						isBalance = false;
						return isBalance;
					}
				operandStack.push(nextCharacter);
				break;

			case ')':
			case ']':
				hasPreOpera = false;
				hasNexOpera = false;
				isback = false;
				isfront = false;
				if (operandStack.isEmpty()) {
					isBalance = false;
					return isBalance;
				}

				char newchar = (char) operandStack.pop();
				// the top of stack ( or [ dosen't match the char from infix string
				if (!(newchar == '(' && nextCharacter == ')' || newchar == '[' && nextCharacter == ']')) {
					isBalance = false;
					return isBalance;
				}
				// case match
				if (charpos != infix.length() - 1) // end of infix string ?
					if (Character.isDigit(infix.charAt(charpos + 1))) {
						isBalance = false; // case the ) ] and next char is digit
						return isBalance;
					}

				break;

			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
				hasNexOpera = false;
				hasNexOpera = false;
				isback = false;
				isfront = false;
				if (charpos != infix.length() - 1) {// if the operation is not in the last char
					// if the next character is not digit or [ or ( or a space
					if ((Character.isDigit(infix.charAt(charpos + 1)) || (infix.charAt(charpos + 1) == '('
							|| (infix.charAt(charpos + 1) == '[' || (infix.charAt(charpos + 1) == ' ')))))

						break;
				}

				isBalance = false;

				break;

			case 32:
				// the current char is space and previos and next char is digit --> equation is
				// not vaild
				if (Character.isDigit(infix.charAt(charpos - 1)))
					isback = true;
				if (Character.isDigit(infix.charAt(charpos + 1)))
					isfront = true;
				// the current char is space and previos and next char is operator --> equation
				// is not vaild
				if (isOperator(infix.charAt(charpos - 1)))
					hasPreOpera = true;
				if (isOperator(infix.charAt(charpos + 1)))
					hasNexOpera = true;

				if (isfront && isback || hasNexOpera && hasPreOpera) {
					isBalance = false;
				}

				break;

			default:// number
				hasPreOpera = false;
				hasNexOpera = false;
				isback = false;
				isfront = false;
				break;
			}
			charpos++;
		}
		// while end
		if (!operandStack.isEmpty())// Check if the stack is not empty ---> not valid
			isBalance = false;
		// else the equation is valid
		return isBalance;
	}

	// method to evaluate 2 operands and one operator
	private double eval(double num1, double num2, char ch) {
		switch (ch) {
		case '+':
			return num2 + num1;

		case '-':
			return num2 - num1;

		case '*':
			return num2 * num1;

		case '/':
			if (num1 != 0)
				return num2 / num1;
			else
				return Double.MAX_VALUE;

		case '^':
			return Math.pow(num2, num1);

		}
		return -1;

	}

	// method returns true if the char is operator
	private boolean isOperator(char ch) {
		switch (ch) {
		case '+':
		case '-':
		case '*':
		case '/':
		case '^':
			return true;
		default:
			return false;
		}

	}

}
