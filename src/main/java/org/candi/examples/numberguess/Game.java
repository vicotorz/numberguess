package org.candi.examples.numberguess;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class Game implements Serializable {
	/**
    * 
    */
	private static final long serialVersionUID = 991300443278089016L;
	private int flag;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	private int number;

	private int guess;
	private int smallest;

	@Inject
	@MaxNumber
	private Integer maxNumber;

	private int biggest;
	private int remainingGuesses;

	private int year;
	private int month;

	@Inject
	@Random
	Instance<Integer> randomNumber;
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	public Game() {
	}

	public int getNumber() {
		return number;
	}

	public int getGuess() {
		return guess;
	}

	public void setGuess(int guess) {
		this.guess = guess;
	}

	public int getSmallest() {
		return smallest;
	}

	public int getBiggest() {
		return biggest;
	}

	public int getRemainingGuesses() {
		return remainingGuesses;
	}

	public void check() {
		System.out.println("调用check方法");
		System.out.println(guess);
		System.out.println(number);
		System.out.println(biggest);
		System.out.println(smallest);
		if (guess > number) {
			biggest = guess - 1;
		} else if (guess < number) {
			smallest = guess + 1;
		} else if (guess == number) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Correct!"));
		}
		remainingGuesses--;
	}

	@PostConstruct
	public void reset() {
		this.smallest = 0;
		this.guess = 0;
		this.remainingGuesses = 10;
		this.biggest = maxNumber;
		this.number = randomNumber.get();
	}
	
	public void checkinfo() {
		if (year <= 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Wrony Year!"));
			flag = 0;
		} else if (month < 1 || month > 12) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Wrony Month"));
			flag = 0;
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("时间验证正确!"));
			flag = 1;
			int temp;
			if (getMonth() == 2) {
				if (getYear() % 4 != 0) {
					temp = 29;

				} else {
					temp = 28;
				}
			} else if (getMonth() == 4 || getMonth() == 6 || getMonth() == 9
					|| getMonth() == 11) {
				temp = 30;
			} else {
				temp = 31;
			}
			System.out.println("经过判断，最终的maxNumber值为" + maxNumber);
			new Generator(temp);
			this.smallest = 0;
			this.guess = 0;
			this.remainingGuesses = 10;
			this.biggest = maxNumber;
			this.number = randomNumber.get();
		}
	}

	public void validateNumberRange(FacesContext context,
			UIComponent toValidate, Object value) {
		if (remainingGuesses <= 0) {
			FacesMessage message = new FacesMessage("No guesses left!");
			context.addMessage(toValidate.getClientId(context), message);
			((UIInput) toValidate).setValid(false);
			return;
		}
		int input = (Integer) value;

		if (input < smallest || input > biggest) {
			((UIInput) toValidate).setValid(false);

			FacesMessage message = new FacesMessage("Invalid guess");
			context.addMessage(toValidate.getClientId(context), message);
		}
	}
}
