package model;

import java.util.*;

public class Expert {
	public List getBrands(String color){

	List brands = new ArrayList();
	
	if(color.equals("amber"))
	{
		brands.add("Jack Amber");
		brands.add("Red moose");
		
	} else
	{
		brands.add("Jail ale ale");
		brands.add("Gout Stout");
		
	}
	return brands;
	}
}
