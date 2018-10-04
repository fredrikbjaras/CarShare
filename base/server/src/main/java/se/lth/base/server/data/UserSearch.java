package se.lth.base.server.data;

import java.util.ArrayList;
import java.util.List;
import Routes.java;
import RoutesDataAccess.java;

public class UserSearch {

	private List<User> userList;
	private UserFilter filter;
	private List<Routes> routeList;

	public UserSearch(List<User> userList, UserFilter filter) {

		/*
		 * filter could be either String name, int telephoneNum or int routeID
		 * 
		 */
		this.filter = filter;
		this.userList = userList;
	}

	// if filter contains routeId
	public UserSearch(List<User> userList, List<Routes> routeList, UserFilter filter) {

		/*
		 * filter could be either String name, int telephoneNum or int routeID
		 * 
		 */
		this.filter = filter;
		this.userList = userList;
		this.routeList = routeList;
	}
	
	public List<User> sortByPhoneNbr(int phoneNbr) {
		List<User> tempUserList = new ArrayList<User>();
		for (int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getPhoneNr() == phoneNbr) {
				tempUserList.add(userList.get(i));
			}
		}
		return tempUserList;
	}

	public List<User> sortByName(String name) {

		List<User> tempUserList = new ArrayList<User>();

		for (int i = 0; i < userList.size(); i++) {
			//checks if parts of the UserNames matches the String name 
			for (int j = 0; j < userList.get(i).getName().length() - name.length(); j++) {

				if (userList.get(i).getName().substring(j, name.length()).equals(name)
						&& !tempUserList.contains(userList.get(i))) {
					tempUserList.add(userList.get(i));
				}
			}
		}
		return tempUserList;
	}

	//
	public List<User> sortByRouteId(int RouteId) {
		List<User> tempUserList = new ArrayList<User>();// list to return
		List<Integer> passengersInRoute; // saves ID of people in route
		int driverId;
		boolean doesRouteExist = false;

		// find the Users on specified route and gets there userID
		for (int i = 0; i < routeList.size(); i++) {
			if (routeList.get(i).getRouteId() == RouteId) {
				passengersInRoute = routeList.get(i).getPassengers();
				driverId = routeList.get(i).getDriver();
				doesRouteExist = true;
				break;
			}
		}
		
		//no route  with the specified RouteId was found, return an empty array. 
		if(!doesRouteExist) {
			return tempUserList;
		}

		// first add the driver to the List so driver is always located at index 0 in
		// the returned list.
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getId() == driverId) {
				tempUserList.add(userList.get(i));
			}
		}

		// then add the passengers
		for (int i = 0; i < userList.size(); i++) {
			for (int j = 0; j < passengersInRoute.size(); j++) {
				if (userList.get(i).getId() == passengersInRoute.get(j)) {
					tempUserList.add(userList.get(i));
					passengersInRoute.remove(j);
				}

			}

		}
		return tempUserList;
	}

}
