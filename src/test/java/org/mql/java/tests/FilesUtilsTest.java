package org.mql.java.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mql.java.utils.FilesUtils;
import org.mql.java.utils.validator.Validator;
import org.mql.java.utils.validator.validatorImp.ClassValidator;
import org.mql.java.utils.validator.validatorImp.PackageValidator;
import org.mql.java.utils.validator.validatorImp.ProjectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

public class FilesUtilsTest {
    private FilesUtils filesUtils;
    private File testFolder;

    @BeforeEach
    void init() {
        Validator<File, Logger> projectValidator = new ProjectValidator();
        Validator<File, Logger> packageValidator = new PackageValidator();
        Validator<File, Logger> classValidator = new ClassValidator();

        filesUtils = new FilesUtils(projectValidator, packageValidator, classValidator);
        testFolder = new File("testFolder");
        testFolder.mkdirs();
        File package1 = new File(testFolder, "package1");
        package1.mkdirs();
        File package2 = new File(testFolder, "package2");
        package2.mkdirs();
        File classFile1 = new File(package1, "Class1.class");
        File classFile2 = new File(package2, "Class2.class");
        try {
            classFile1.createNewFile();
            classFile2.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testIsAValidProject() {
        assertTrue(filesUtils.isAValidProject(testFolder));
    }

    @Test
    void testGetAllPackages() {
        List<File> packages = new ArrayList<>();
        filesUtils.getAllPackages(testFolder, packages);
        assertEquals(2, packages.size());
    }

    @Test
    void testIsAValidPackage() {
        File package1 = new File(testFolder, "package1");
        assertTrue(filesUtils.isAValidPackage(package1));
    }

    @Test
    void testIsAValidClassFile() {
        File classFile1 = new File(testFolder, "package1/Class1.class");
        assertTrue(filesUtils.isAValidClassFile(classFile1));
    }
}
