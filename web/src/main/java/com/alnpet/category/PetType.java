package com.alnpet.category;

public enum PetType {
	UNKNOWN(0, "Unknown"),

	SMALL(1, "Small"),

	MIDDLE(2, "Middle"),

	BIG(3, "Big");

	private int m_id;

	private String m_name;

	private PetType(int id, String name) {
		m_id = id;
		m_name = name;
	}

	public static PetType getById(int id, PetType defaultAction) {
		for (PetType action : PetType.values()) {
			if (action.getId() == id) {
				return action;
			}
		}

		return defaultAction;
	}

	public static PetType getByName(String name, PetType defaultAction) {
		for (PetType action : PetType.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultAction;
	}

	public int getId() {
		return m_id;
	}

	public String getName() {
		return m_name;
	}
}
