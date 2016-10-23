package dto;

import java.io.Serializable;

public abstract class Entity implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private int id;

	public Entity() {
	}

	public Entity(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
//TODO сериализацию, глянуть здесь http://info.javarush.ru/translation/2014/10/06/%D0%97%D0%B0%D1%87%D0%B5%D0%BC-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-SerialVersionUID-%D0%B2%D0%BD%D1%83%D1%82%D1%80%D0%B8-Serializable-%D0%BA%D0%BB%D0%B0%D1%81%D1%81%D0%B0-%D0%B2-Java.html