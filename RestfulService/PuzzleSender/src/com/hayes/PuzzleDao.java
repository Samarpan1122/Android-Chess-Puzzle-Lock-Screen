package com.hayes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import com.hayes.Puzzle;

public class PuzzleDao {
    @SuppressWarnings("unchecked")
    public List<Puzzle> getAllPuzzles(@Context ServletContext context) {
        List<Puzzle> puzzleList = new ArrayList<Puzzle>();
        BufferedReader br = null;
        try {
            // Corrected the method to get the resource stream from the ServletContext
            InputStream stream = context.getResourceAsStream("/WEB-INF/data/Puzzles.txt");
            if (stream != null) {
                br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String strLine;

                while ((strLine = br.readLine()) != null) {
                    String name = strLine;
                    String initialLayout = br.readLine();
                    String finalLayout = br.readLine();
                    puzzleList.add(new Puzzle(name, initialLayout, finalLayout));
                }
            } else {
                // Handle the case where the file is not found
                System.err.println("Puzzles.txt not found in the specified path.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure the BufferedReader is closed properly
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return puzzleList;
    }
}