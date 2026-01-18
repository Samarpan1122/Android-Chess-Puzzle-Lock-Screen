package com.hayes;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/PuzzleService")
public class PuzzleService {

    // Initialize PuzzleDao instance
    private final PuzzleDao puzzleDao = new PuzzleDao();

    // Inject ServletContext
    @Context
    private ServletContext context;

    @GET
    @Path("/puzzles")
    @Produces(MediaType.APPLICATION_XML)
    public List<Puzzle> getPuzzles() {
        // Fetch all puzzles using the PuzzleDao and the provided context
        return puzzleDao.getAllPuzzles(context);
    }
}