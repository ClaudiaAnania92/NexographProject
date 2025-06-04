package com.imp.projectdbms.repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.imp.projectdbms.model.Employee;
import static org.neo4j.driver.Values.parameters;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {

	@Autowired
	private Driver driver;
	
	public boolean managerExists(String managerName) {
	    try (Session session = driver.session()) {
	        String query = "MATCH (m:Employee {name: $name}) RETURN COUNT(m) > 0 AS exists";
	        return session.executeRead(tx ->
	            tx.run(query, parameters("name", managerName))
	              .single()
	              .get("exists")
	              .asBoolean()
	        );
	    }
	}
	
	public boolean employeeExists(int empId) {
	    try (Session session = driver.session()) {
	        return session.executeRead(tx -> {
	            String query = "MATCH (e:Employee {emp_id: $empId}) RETURN e";
	            var result = tx.run(query, parameters("empId", empId));
	            return result.hasNext();
	        });
	    } catch (Exception e) {
	        System.err.println("Errore nel controllo esistenza employee con ID " + empId);
	        System.err.println("Dettaglio: " + e.getMessage());
	        return false;
	    }
	}



	public boolean saveEmployee(String name, int empId) {
	    if (employeeExists(empId)) {
	        System.err.println("Employee con ID " + empId + " già esistente.");
	        return false;
	    }
	    try (Session session = driver.session()) {
	        Boolean result = session.executeWrite(tx -> {
	            String query = "CREATE (e:Employee {emp_id: $empId, name: $name})";
	            tx.run(query, parameters("empId", empId, "name", name));
	            return true;
	        });
	        return result; //return result != null && result;	-> questo sarebbe più sicuro ma se non vogliamo null possiamo non scriverlo
	    } catch (Exception e) {
	        System.err.println("Errore nel salvataggio dell'employee " + name + " con ID " + empId);
	        System.err.println("Dettaglio: " + e.getMessage());
	        return false;
	    }
	}



	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		try (Session session = driver.session()) {
			String cypher = "MATCH (e:Employee) RETURN e.emp_id AS empId, e.name AS name ORDER BY e.emp_id DESC";
			Result result = session.run(cypher);
			while (result.hasNext()) {
				Record record = result.next();
				int empId = record.get("empId").asInt();
				String name = record.get("name").asString();
				employees.add(new Employee(empId, name));
			}
		} catch (Exception e) {
			System.err.println("Errore durante il recupero degli employee");
			System.err.println("Dettaglio: " + e.getMessage());
		}
		return employees;
	}

	public List<Employee> getVaderEmployee() {
		List<Employee> vaderEmployees = new ArrayList<>();
		try (Session session = driver.session()) {
			String cypher = "MATCH (boss:Employee {name: 'Darth Vader'})<-[:REPORTS_TO]-(e:Employee) "
					+ "RETURN e.emp_id AS empId, e.name AS name";
			Result result = session.run(cypher);
			while (result.hasNext()) {
				Record record = result.next();
				int empId = record.get("empId").asInt();
				String name = record.get("name").asString();
				vaderEmployees.add(new Employee(empId, name));
			}
		} catch (Exception e) {
			System.err.println("Errore durante il recupero degli employee di Vader");
			System.err.println("Dettaglio: " + e.getMessage());
		}
		return vaderEmployees;
	}

	public List<Employee> getEmployeesByManager(String managerName) {
		List<Employee> employees = new ArrayList<>();
		try (Session session = driver.session()) {
			String cypher = "MATCH (teamMember:Employee)-[:REPORTS_TO]->(manager:Employee {name: $managerName})\r\n"
					+ "RETURN teamMember.emp_id AS empId, teamMember.name AS name\r\n";
			Result result = session.run(cypher, parameters("managerName", managerName));
			while (result.hasNext()) {
				var record = result.next();
				int empId = record.get("empId").asInt();
				String name = record.get("name").asString();
				employees.add(new Employee(empId, name));
			}
		} catch (Exception e) {
			System.err.println("Errore durante il recupero degli impiegati di " + managerName);
			System.err.println("Dettaglio: " + e.getMessage());
		}
		return employees;
	}

	public List<Employee> getJacobTeam() {
		List<Employee> jacobTeam = new ArrayList<>();
		try (Session session = driver.session()) {
			String cypher = "MATCH (jacob:Employee {name: 'Jacob'}) "
					+ "MATCH (teamMember:Employee)-[:REPORTS_TO*]->(jacob) "
					+ "WHERE NOT (teamMember)-[:IS_FRIENDS_WITH]-(jacob) "
					+ "RETURN teamMember.emp_id AS empId, teamMember.name AS name";
			Result result = session.run(cypher);
			while (result.hasNext()) {
				Record record = result.next();
				int empId = record.get("empId").asInt();
				String name = record.get("name").asString();
				jacobTeam.add(new Employee(empId, name));
			}
		} catch (Exception e) {
			System.err.println("Errore durante il recupero degli employee di Jacob");
			System.err.println("Dettaglio: " + e.getMessage());
		}
		return jacobTeam;
	}

	public List<Employee> getManagerTeam(String managerName) {
		List<Employee> team = new ArrayList<>();
		try (Session session = driver.session()) {
			String cypher = "MATCH (manager:Employee {name: $managerName})\n"
					+ "MATCH (manager)-[:REPORTS_TO*]->(teamMember:Employee)\n"
					+ "WHERE NOT (teamMember)-[:IS_FRIENDS_WITH]-(manager)\n"
					+ "RETURN teamMember.emp_id AS empId, teamMember.name AS name";
			Result result = session.run(cypher, parameters("managerName", managerName));
			while (result.hasNext()) {
				var record = result.next();
				int empId = record.get("empId").asInt();
				String name = record.get("name").asString();
				team.add(new Employee(empId, name));
			}
		} catch (Exception e) {
			System.err.println("Errore durante il recupero del team di " + managerName);
			System.err.println("Dettaglio: " + e.getMessage());
		}
		return team;
	}

}
