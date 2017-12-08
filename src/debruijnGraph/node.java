package debruijnGraph;
public class node {
	private String label;
	private char parent;
	public node(String text) {
		this.label = text;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLabel() {
		return this.label;
	}
	public void setParent(char c) {
		this.parent = c;
	}
	public char getParent() {
		return this.parent;
	}
	public String translate() {
		return "";
	}
}