/**
 * The `Constants` package contains classes that define various constants and configurations
 * used throughout the application.
 * 
 * @author Rohit
 */
package Constants;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The `ProjectConfig` class stores configuration constants for the project.
 * It includes the path to map files used in the application.
 * 
 * @author Rohit
 * @version build 1
 */
public class ProjectConfig {

	private ProjectConfig(){}

	  /**
     * The path to map files in the project.
     */
	public static final String  D_MAP_FILES_PATH ;
	
	static {
        // Initialize the path to map files based on the project's current position
		Path currentPathPosition = Paths.get("");
		D_MAP_FILES_PATH = currentPathPosition.toAbsolutePath()+"/src/main/java/map/";
	}

}
