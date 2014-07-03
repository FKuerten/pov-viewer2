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

@RequiredArgsConstructor
public class PovRayImageSource extends AbstractImageSource {
    
    private final File directory;
    
    String getBaseName(final ImmutableCamera camera, final int w, final int h) {
        StringBuilder sb = new StringBuilder();
        sb.append(camera.getAsString().replaceAll("/", "."));
        sb.append("@").append(w).append("x").append(h);
        return sb.toString().replaceAll("@", "at").replaceAll("<", "(")
            .replaceAll(">", ")").replaceAll("\\[", "(").replaceAll("\\]", ")")
            .replaceAll("[\\(\\)]", "_");
    }

    void printScene(PrintWriter p, ImmutableScene scene) {
        p.format("include \"%s\"", scene.getName()).println();
    }

    @SuppressWarnings("boxing")
    void printCamera(PrintWriter p, ImmutableCamera c) {
        p.println("camera {");
        {
            p.println("\tperspective");
            p.format(Locale.ENGLISH, "\tangle %f", c.getFOV()).println();
            p.format(Locale.ENGLISH, "\trotate %f * x", -c.getTheta())
                .println();
            p.format(Locale.ENGLISH, "\trotate %f * y", -c.getPhi()).println();
            p.format(Locale.ENGLISH, "\ttranslate <%.2f, %.2f, %.2f>",
                c.getX(), c.getY(), c.getZ()).println();
        }
        p.println("}");
    }
    
    void printPovFile(PrintWriter p, ImmutableCamera camera) {
        this.printScene(p, camera.getScene());
        this.printCamera(p, camera);
    }
    
    private void printIniFile(PrintWriter pw, final int w, final int h,
            final String povFileName, final double levelOfDetail) {
        pw.println("+W" + w + " +H" + h);
        pw.println("Input_File_Name=\"" + povFileName + "\"");
        // pw.println("Quality=" + );
        // pw.println("Declare=CONFIGURATION=" +
        // this.camera.getConfiguration());
        pw.println("Declare=LEVEL_OF_DETAIL=" + levelOfDetail);
    }
    
    private void dumpIniFile(final String iniFileName, final int w,
            final int h, final String povFileName, final double levelOfDetail)
            throws FileNotFoundException {
        assert this.directory.isDirectory();
        File file = new File(this.directory, iniFileName);
        if (!file.exists()) {
            PrintWriter pw = new PrintWriter(file);
            printIniFile(pw, w, h, povFileName, levelOfDetail);
            pw.close();
        } else {
            // System.out.println("skipping file creation");
        }
    }

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
    
    boolean hasImage(ImmutableCamera camera, int w, int h) {
        final String baseName = getBaseName(camera, w, h);
        final String iniFileName = baseName.concat(".cam.ini");
        final String povFileName = baseName.concat(".cam.pov");
        final String imageFileName = baseName.concat(".cam.png");
        File iniFile = new File(this.directory, iniFileName);
        File povFile = new File(this.directory, povFileName);
        return !this.needsRebuild(iniFile, povFile, imageFileName);
    }

    @Override
    BufferedImage getImage(ImmutableCamera camera, int w, int h) {
        try {
            final String baseName = getBaseName(camera, w, h);
            final String iniFileName = baseName.concat(".cam.ini");
            final String povFileName = baseName.concat(".cam.pov");
            final String imageFileName = baseName.concat(".cam.png");
            this.dumpIniFile(iniFileName, w, h, povFileName,
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
    
    private BufferedImage conditionallyCreateImage(File iniFile, File povFile,
            String imageFileName) {
        boolean needsRebuild = needsRebuild(iniFile, povFile, imageFileName);
        File imageFile = new File(this.directory, imageFileName);
        if (needsRebuild) {
            this.unconditionallyCreateImage(iniFile, povFile, imageFile);
        }
        assert imageFile.exists();
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
    
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
    
    private void unconditionallyCreateImage(File iniFile, File povFile,
            File imageFile) {
        List<String> args = new ArrayList<String>();
        args.add("../../render.sh");
        args.add("\"" + iniFile.getName() + "\"");
        
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(this.directory);
        pb.inheritIO();
        
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
