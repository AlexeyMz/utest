package test;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.*;

public class Cd {
	private String id;

	private Cd bonusCd;

	Cd(String id, Cd bonusCd) {
		this.id = id;
		this.bonusCd = bonusCd;
	}

	Cd(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Cd getBonusCd() {
		return bonusCd;
	}

	public static void main(String[] args) {
		Cd bj = new Cd("basement_jaxx_singles");
		Cd mr = new Cd("maria rita");

		/*
		 * List order = new ArrayList(); order.add(mr); // ������� ��� ���� ����
		 * � ��� �� ������ (��� ������ �� ���� � ��� �� ������) order.add(bj);
		 * order.add(bj);
		 * 
		 * // ������� � ������ ��� ������11 (���������������) order.add(order);
		 */

		XStream xstream = new XStream();
		// xstream.alias("cd", Cd.class);
		System.out.println(xstream.toXML(bj));
	}
}
