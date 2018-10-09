package se.lth.base.server.data;

import java.util.ArrayList;
import java.util.List;

public class UserSearch {

	private List<User> userList;
	private UserFilter filter;
	private List<Route> routeList;
	private int userId;

	public UserSearch(List<User> userList, UserFilter filter) {

		this.filter = filter;
		this.userList = userList;
		userId = -1; // so we now that we call was made by an admin
	}

	// if filter contains routeId
	public UserSearch(List<User> userList, List<Route> routeList, UserFilter filter) {

		this.filter = filter;
		this.userList = userList;
		this.routeList = routeList;
		userId = -1; //so we now that we call was made by an admin
	}
	
	// if filter contains routeId and call was NOT made by an admin 
	public UserSearch(List<User> userList, List<Route> routeList, UserFilter filter, int userId) {

		this.filter = filter;
		this.userList = userList;
		this.routeList = routeList;
		this.userId = userId;
	}
	
	
	// is supposed to return the sorted list dependent on the filter. 
	// due to filter class is not implemented this method can't be implemented yet.
	public List<User> returnSortedList(){
		List<User> tempUserList = new ArrayList<User>();
		return tempUserList;
	}
	
	//returns an empty array if no User with matching phone number was found
	private List<User> sortByPhoneNbr(String phoneNbr) {
		List<User> tempUserList = new ArrayList<User>();
		for (int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getPhoneNr() == phoneNbr) {
				tempUserList.add(userList.get(i));
			}
		}
		return tempUserList;
	}
	
	//returns an empty array if no User with matching name was found
	private List<User> sortByName(String name) {
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

	
	private List<User> sortByRouteId(int RouteId) {
		List<User> tempUserList = new ArrayList<User>();// list to return
		List<Integer> passengersInRoute = new ArrayList<Integer>(); // saves ID of people in route
		int driverId = 0;
		boolean doesRouteExist = false;

		// find the Users on specified route and gets there userID
		for (int i = 0; i < routeList.size(); i++) {
			if (routeList.get(i).getRouteID() == RouteId) {
				String passengerString = routeList.get(i).getPassengers();
				driverId = routeList.get(i).getDriverID();
				doesRouteExist = true;
				break;
			}
		}
		
		//if caller is NOT an admin, the callers userID must be in the route, as either driver or passenger
		if(userId != -1) {
			if(driverId != userId && !passengersInRoute.contains(userId)) {
				//error user, flags to UserResource that the user dosen't exist in the route
				User errorUser = new User(-1, "I am Error", "Error","404",false);
				tempUserList.add(errorUser);
				return tempUserList;
			}
		}
		
		//no route  with the specified RouteId was found, return an empty array. 
		if(!doesRouteExist) {
			return tempUserList;
		}

		// first add the driver to the List so driver is always located at index 0 in
		// the returned list.
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUserID() == driverId) {
				tempUserList.add(userList.get(i));
			}
		}

		// then add the passengers
		for (int i = 0; i < userList.size(); i++) {
			for (int j = 0; j < passengersInRoute.size(); j++) {
				if (userList.get(i).getUserID() == passengersInRoute.get(j)) {
					tempUserList.add(userList.get(i));
					passengersInRoute.remove(j);
				}

			}

		}
		return tempUserList;
	}

}
