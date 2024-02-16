import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final String filePath;
    private final Map<String, String> users = new HashMap<>();

    public UserManager(String filePath) {
        this.filePath = filePath;
        initializeFile();
        loadUsers();
    }
    
    /**
	 * Load the users in the file
	 */
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
	 * Validate if users is in map of users
	 * 
	 * @param username 
	 * @return boolean true or false
	 */
    public synchronized boolean validateUser(String username) {
        return users.containsKey(username);
    }
    
    /**
	 * Validate if it's the correct password 
	 * 
	 * @param username and password
	 * @return boolean true or false
	 */
    public synchronized boolean validatePassword(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
    
    /**
	 * Add users and password to file and map
	 * 
	 * @param username and password
	 * @return boolean true or false
	 */
    public synchronized boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        saveUser(username, password);
        return true;
    }
    
    /**
	 * Save users in file 
	 * 
	 * @param username and password
	 */
    private void saveUser(String username, String password) {
        try (FileWriter fw = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * Creation file  
	 */
    private void initializeFile() {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                
            } else {
                // file already exists
            }
        } catch (IOException e) {

        }
        
        /*
        File file = new File(filePath);
            // boolean deleted = file.delete();
            // if (!deleted) {
            // System.out.println("Failed to delete existing file.");
            // }
        } else {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    System.out.println("Failed to create new file.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

    }
}
