package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WordCount {

	private String word;
	private long count;
	private long windowStart;
	private long windowEnd;
	private String language;

	static final String[] LANGUAGES = {"english", "french", "hindi"};
	static final Random random = new Random();

	public WordCount() {

	}

	public WordCount(String word, long count, long windowStart, long windowEnd) {
		this.word = word;
		this.count = count;
		this.windowStart = windowStart;
		this.windowEnd = windowEnd;
		language = LANGUAGES[random.nextInt(3)];
	}

	public String getLanguage() {
		return language;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(long windowStart) {
		this.windowStart = windowStart;
	}

	public long getWindowEnd() {
		return windowEnd;
	}

	public void setWindowEnd(long windowEnd) {
		this.windowEnd = windowEnd;
	}

	@Override
	public String toString() {
		return "WordCount{" +
				"word='" + word + '\'' +
				", count=" + count +
				", windowStart=" + windowStart +
				", windowEnd=" + windowEnd +
				'}';
	}
}
