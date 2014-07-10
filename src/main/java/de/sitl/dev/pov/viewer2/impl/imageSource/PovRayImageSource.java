package de.sitl.dev.pov.viewer2.impl.imageSource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import lombok.RequiredArgsConstructor;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;

/**
 * An image source for povray.
 * 
 * @author Fabian K&uuml;rten
 */
@RequiredArgsConstructor
public class PovRayImageSource extends AbstractImageSource {
    
    /**
     * The cache directory.
     */
    private final File directory;
    
    /**
     * Returns a unique string representation of the requested task.
     * 
     * @param camera
     *            the camera
     * @param w
     *            width of the image
     * @param h
     *            height of the image
     * @return unique string representation
     */
    String getBaseName(final ImmutableCamera camera) {
        String strBase = camera.getAsString();
        strBase = strBase.replaceAll("/", ".");
        // strBase = strBase.replaceAll("<", "(");
        // strBase = strBase.replaceAll(">", ")");
        strBase = strBase.replaceAll("\\[", "(");
        strBase = strBase.replaceAll("\\]", ")");
        strBase = strBase.replaceAll("[\\(\\)]", "_");
        return strBase;
    }
    
    String getBaseNameWithSizes(final ImmutableCamera camera, final int w,
            final int h) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getBaseName(camera));
        sb.append("@").append(w).append("x").append(h);
        return sb.toString();
    }
    
    String getPovName(final ImmutableCamera camera) {
        return this.getBaseName(camera).concat(".cam.pov");
    }
    
    String getIniName(final ImmutableCamera camera, final int w, final int h) {
        return this.getBaseNameWithSizes(camera, w, h).concat(".cam.ini");
    }
    
    String getPngName(final ImmutableCamera camera, final int w, final int h) {
        return this.getBaseNameWithSizes(camera, w, h).concat(".cam.png");
    }

    /**
     * Prints the scene information.
     * 
     * @param p
     *            where to print
     * @param scene
     *            the scene
     */
    void printScene(PrintWriter p, ImmutableScene scene) {
        p.format("include \"%s\"", scene.getName()).println();
    }
    
    /**
     * Prints the camera information
     * 
     * @param p
     *            where to print
     * @param c
     *            the camera
     */
    @SuppressWarnings("boxing")
    void printCamera(PrintWriter p, ImmutableCamera c) {
        p.println("camera {");
        {
            p.println("\tperspective");
            p.format(Locale.ENGLISH, "\tup y / sqrt(image_width/image_height)")
                .println();
            p.format(Locale.ENGLISH,
                "\tright x * sqrt(image_width/image_height)")
                .println();
            p.format(Locale.ENGLISH, "\tangle %f", c.getFOV()).println();
            p.format(Locale.ENGLISH, "\trotate %f * x", -c.getTheta())
                .println();
            p.format(Locale.ENGLISH, "\trotate %f * y", -c.getPhi()).println();
            p.format(Locale.ENGLISH, "\ttranslate <%.2f, %.2f, %.2f>",
                c.getX(), c.getY(), c.getZ()).println();
        }
        p.println("}");
    }
    
    @SuppressWarnings("boxing")
    void printSpotlight(PrintWriter p, ImmutableCamera c) {
        p.println("light_source {");
        p.println("\t<0, 0, 0>");
        p.println("\tcolor White");
        p.println("\tspotlight");
        p.println("\tradius 20");
        p.println("\tfalloff 25");
        p.println("\ttightness 5");
        
        p.println("\tpoint_at <0, 0, 1>");
        p.format(Locale.ENGLISH, "\trotate %f * x", -c.getTheta()).println();
        p.format(Locale.ENGLISH, "\trotate %f * y", -c.getPhi()).println();
        p.format(Locale.ENGLISH, "\ttranslate <%.2f, %.2f, %.2f>", c.getX(),
            c.getY(), c.getZ()).println();
        p.println("}");
    }

    /**
     * Prints a full pov file: An include statement and a camera.
     * 
     * @param p
     *            where to print
     * @param camera
     *            the camera.
     */
    void printPovFile(PrintWriter p, ImmutableCamera camera) {
        this.printScene(p, camera.getScene());
        this.printCamera(p, camera);
        if (camera.hasSpotlight()) {
            this.printSpotlight(p, camera);
        }
    }
    
    /**
     * Prints the ini file.
     * 
     * @param pw
     *            where to print
     * @param w
     *            the width of the image
     * @param h
     *            the height of the image
     * @param povFileName
     *            name of the corresponding pov file
     * @param levelOfDetail
     *            the level of detail
     */
    private void printIniFile(PrintWriter pw, final int w, final int h,
            final String povFileName, final String imageFileName,
            final double levelOfDetail) {
        pw.println("+W" + w + " +H" + h);
        pw.println("Input_File_Name=\"" + povFileName + "\"");
        pw.format("Output_File_Name=\"%s\"", imageFileName).println();
        // pw.println("Quality=" + );
        // pw.println("Declare=CONFIGURATION=" +
        // this.camera.getConfiguration());
        pw.println("Declare=LEVEL_OF_DETAIL=" + levelOfDetail);
    }
    
    /**
     * Dumps the ini file to disk
     * 
     * @param iniFileName
     *            the file name
     * @param w
     *            the image's width
     * @param h
     *            the image's height
     * @param povFileName
     *            the pov file name
     * @param levelOfDetail
     *            the level of detail
     * @throws FileNotFoundException
     *             If the given file object does not denote an existing,
     *             writable regular file and a new regular file of that name
     *             cannot be created, or if some other error occurs while
     *             opening or creating the file
     */
    private void dumpIniFile(final String iniFileName, final int w,
            final int h, final String povFileName, final String imageFileName,
            final double levelOfDetail) throws FileNotFoundException {
        assert this.directory.isDirectory();
        File file = new File(this.directory, iniFileName);
        if (!file.exists()) {
            PrintWriter pw = new PrintWriter(file);
            printIniFile(pw, w, h, povFileName, imageFileName, levelOfDetail);
            pw.close();
        } else {
            // System.out.println("skipping file creation");
        }
    }

    /**
     * Dumps the pov file to disk
     * 
     * @param fileName
     *            the pov file's name
     * @param camera
     *            the camera
     * @throws FileNotFoundException
     *             If the given file object does not denote an existing,
     *             writable regular file and a new regular file of that name
     *             cannot be created, or if some other error occurs while
     *             opening or creating the file
     */
    private void
            dumpPovFile(final String fileName, final ImmutableCamera camera)
                    throws FileNotFoundException {
        assert this.directory.isDirectory();
        File file = new File(this.directory, fileName);
        if (!file.exists()) {
            PrintWriter pw = new PrintWriter(file);
            this.printPovFile(pw, camera);
            pw.close();
        }
    }
    
    @Override
    boolean hasImageInCache(ImmutableCamera camera, int w, int h) {
        final String povFileName = this.getPovName(camera);
        final String iniFileName = this.getIniName(camera, w, h);
        final String imageFileName = this.getPngName(camera, w, h);
        File iniFile = new File(this.directory, iniFileName);
        File povFile = new File(this.directory, povFileName);
        return !this.needsRebuild(iniFile, povFile, imageFileName);
    }

    @Override
    BufferedImage getImage(ImmutableCamera camera, int w, int h) {
        try {
            final String povFileName = this.getPovName(camera);
            final String iniFileName = this.getIniName(camera, w, h);
            final String imageFileName = this.getPngName(camera, w, h);
            this.dumpIniFile(iniFileName, w, h, povFileName, imageFileName,
                camera.getLevelOfDetail());
            this.dumpPovFile(povFileName, camera);
            
            File iniFile = new File(this.directory, iniFileName);
            File povFile = new File(this.directory, povFileName);
            return this.conditionallyCreateImage(iniFile, povFile,
                imageFileName);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
    
    /**
     * Creates an image if needed. It is needed if it does not exist or is too
     * old.
     * 
     * @param iniFile
     *            the ini file
     * @param povFile
     *            the pov file
     * @param imageFileName
     *            the name of the image file
     * @return the generated or cached image
     */
    private BufferedImage conditionallyCreateImage(File iniFile, File povFile,
            String imageFileName) {
        boolean needsRebuild = needsRebuild(iniFile, povFile, imageFileName);
        File imageFile = new File(this.directory, imageFileName);
        if (needsRebuild) {
            this.unconditionallyCreateImage(iniFile, imageFile);
        }
        for (int i = 0; i < 100; i++ ) {
            if (imageFile.exists()) {
                try {
                    return ImageIO.read(imageFile);
                } catch (IOException e) {
                    // retry later
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // ignored
                }
            }
        }
        throw new Error(String.format("File '%s' not found.", imageFileName));
    }
    
    /**
     * Checks whether the image needs to be created.
     * 
     * @param iniFile
     *            the ini file
     * @param povFile
     *            the pov file
     * @param imageFileName
     *            the image file's name
     * @return whether the image needs to created/updated
     */
    private boolean needsRebuild(File iniFile, File povFile,
            String imageFileName) {
        File imageFile = new File(this.directory, imageFileName);
        if (!iniFile.exists() || !povFile.exists()) {
            return true;
        }
        if (imageFile.exists()) {
            long changeTime =
                Math.max(iniFile.lastModified(), povFile.lastModified());
            if (imageFile.lastModified() > changeTime) {
                // no rebuild needed
                return false;
            }
        }
        return true;
    }
    
    /**
     * Unconditionally calls povray to render the image
     * 
     * @param iniFile
     *            the ini file
     * @param imageFile
     *            the resulting image file
     */
    private void unconditionallyCreateImage(File iniFile, File imageFile) {
        List<String> args = new ArrayList<String>();
        args.add("../../render.sh");
        args.add("\"" + iniFile.getName() + "\"");
        
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(this.directory);
        // pb.inheritIO();
        
        try {
            Process p = pb.start();
            p.waitFor();
        } catch (IOException e) {
            throw new Error(e);
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }
    
}
