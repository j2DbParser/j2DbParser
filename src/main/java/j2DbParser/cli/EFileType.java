package j2DbParser.cli;

public enum EFileType {
	LOG(".log"),
	XML(".xml");

	private String surfix;

	private EFileType(String surfix) {
		this.surfix = surfix;
	}

	public static EFileType is() {
		String file = EOptions.FILE.value();
		for (EFileType type : values()) {
			if (type.isType(file)) {
				return type;
			}
		}
		return null;
	}

	public boolean isType(String file) {
		return file.endsWith(surfix);
	}
}
