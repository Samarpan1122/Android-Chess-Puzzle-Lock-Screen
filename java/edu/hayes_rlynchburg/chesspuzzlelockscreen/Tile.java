package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.hayes_rlynchburg.chesspuzzlelockscreen.Tile.Color.BLACK;
import static edu.hayes_rlynchburg.chesspuzzlelockscreen.Tile.Color.WHITE;

/**
 * Created by Ryan Hayes on 2/6/2017.
 */

public class Tile {

    public enum Color { BLACK, WHITE, NONE }

    private final static int[] BISHOP_CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
    private final static int[] ROOK_CANDIDATE_MOVE_COORDINATES = { -8, -1, 1, 8 };
    private final static int[] QUEEN_CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };
    private final static int[] KING_CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };
    private final static int[] KNIGHT_CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };
    private final static int[] PAWN_CANDIDATE_MOVE_COORDINATES = { 8, 16, 7, 9 };

    Tile(String piece, Color tileColor, int location) {
        tileColor_ = tileColor;
        pieceName_ = piece;
        pieceColor_ = Color.NONE;
        tileLoc_ = location;
        isFirstMove = true;
        if (isWhiteTile()) {
            if ("empty".equals(piece)) {
                id_ = R.drawable.whitesquare;
            }
        } else {
            id_ = R.drawable.blacksquare;
        }
    }

    Tile(String piece, Color tileColor, Color pieceColor, int location) {
        tileColor_ = tileColor;
        pieceColor_ = pieceColor;
        pieceName_ = piece;
        tileLoc_ = location;
        isFirstMove = true;
        if (isWhiteTile()) {
            if (isWhitePiece()) {
                if ("pawn".equals(piece)) {
                    id_ = R.drawable.wpawnwhitesquare;
                } else if ("rook".equals(piece)) {
                    id_ = R.drawable.wrookwhitesquare;
                } else if ("bishop".equals(piece)) {
                    id_ = R.drawable.wbishopwhitesquare;
                } else if ("king".equals(piece)) {
                    id_ = R.drawable.wkingwhitesquare;
                } else if ("queen".equals(piece)) {
                    id_ = R.drawable.wqueenwhitesquare;
                } else if ("knight".equals(piece)) {
                    id_ = R.drawable.wknightwhitesquare;
                }
            } else {
                if ("pawn".equals(piece)) {
                    id_ = R.drawable.bpawnwhitesquare;
                } else if ("rook".equals(piece)) {
                    id_ = R.drawable.brookwhitesquare;
                } else if ("bishop".equals(piece)) {
                    id_ = R.drawable.bbishopwhitesquare;
                } else if ("king".equals(piece)) {
                    id_ = R.drawable.bkingwhitesquare;
                } else if ("queen".equals(piece)) {
                    id_ = R.drawable.bqueenwhitesquare;
                } else if ("knight".equals(piece)) {
                    id_ = R.drawable.bknightwhitesquare;
                }
            }
        } else {
            if (isWhitePiece()) {
                if ("pawn".equals(piece)) {
                    id_ = R.drawable.wpawnblacksquare;
                } else if ("rook".equals(piece)) {
                    id_ = R.drawable.wrookblacksquare;
                } else if ("bishop".equals(piece)) {
                    id_ = R.drawable.wbishopblacksquare;
                } else if ("king".equals(piece)) {
                    id_ = R.drawable.wkingblacksquare;
                } else if ("queen".equals(piece)) {
                    id_ = R.drawable.wqueenblacksquare;
                } else if ("knight".equals(piece)) {
                    id_ = R.drawable.wknightblacksquare;
                }
            } else {
                if ("pawn".equals(piece)) {
                    id_ = R.drawable.bpawnblacksquare;
                } else if ("rook".equals(piece)) {
                    id_ = R.drawable.brookblacksquare;
                } else if ("bishop".equals(piece)) {
                    id_ = R.drawable.bbishopblacksquare;
                } else if ("king".equals(piece)) {
                    id_ = R.drawable.bkingblacksquare;
                } else if ("queen".equals(piece)) {
                    id_ = R.drawable.bqueenblacksquare;
                } else if ("knight".equals(piece)) {
                    id_ = R.drawable.bknightblacksquare;
                }
            }
        }
    }

    private boolean isWhitePiece() {
        return pieceColor_ == WHITE;
    }

    public void format(Tile dest, Context c) {
        String colorChar = "w";
        String oldTileChar = "white";
        String newTileChar = "white";

        if (tileColor_ == BLACK) {
            oldTileChar = "black";
        }

        if (pieceColor_ == BLACK) {
            colorChar = "b";
        }
        if (dest.tileColor_ == BLACK) {
            newTileChar = "black";
        }

        String oldId = oldTileChar + "square";
        String newId = colorChar + pieceName_ + newTileChar + "square";

        dest.pieceName_ = pieceName_;
        dest.pieceColor_ = pieceColor_;
        dest.isFirstMove = false;

        pieceName_ = "empty";
        pieceColor_ = Color.NONE;

        dest.id_ = c.getResources().getIdentifier(newId, "drawable", c.getPackageName());
        id_ = c.getResources().getIdentifier(oldId, "drawable", c.getPackageName());
    }

    public void pawnPromote(final int position, final Context cxt) {
        final CharSequence pieces[] = new CharSequence[]{"knight", "bishop", "queen", "rook"};

        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        builder.setTitle("Pick a Promotion");
        builder.setItems(pieces, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pieceName_ = pieces[which].toString();
            }
        });
        builder.show();
    }

    public boolean isValid(int dest) {
        boolean isValid = true;
        if ("pawn".equals(pieceName_)) {
            isValid = pawnIsValid(dest);
        } else if ("rook".equals(pieceName_)) {
            isValid = rookIsValid(dest);
        } else if ("bishop".equals(pieceName_)) {
            isValid = bishopIsValid(dest);
        } else if ("king".equals(pieceName_)) {
            isValid = kingIsValid(dest);
        } else if ("queen".equals(pieceName_)) {
            isValid = queenIsValid(dest);
        } else if ("knight".equals(pieceName_)) {
            isValid = knightIsValid(dest);
        }
        return isValid;
    }

    public boolean pawnIsValid(int dest) {
        return calculatePawnLegalMoves().contains(dest);
    }

    public Collection<Integer> calculatePawnLegalMoves() {
        final List<Integer> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : PAWN_CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.tileLoc_ + (-1 * currentCandidateOffset);
            if (this.pieceColor_ == BLACK) {
                candidateDestinationCoordinate = this.tileLoc_ + (1 * currentCandidateOffset);
            }
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 && !Board.board_[candidateDestinationCoordinate].isOccupied()) {
                if (this.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(candidateDestinationCoordinate);
                } else {
                    legalMoves.add(candidateDestinationCoordinate);
                }
            } else if (currentCandidateOffset == 16 && this.isFirstMove &&
                    ((BoardUtils.INSTANCE.SECOND_ROW.get(this.tileLoc_) && (this.pieceColor_ == BLACK) ||
                            (BoardUtils.INSTANCE.SEVENTH_ROW.get(this.tileLoc_) && (this.pieceColor_ == WHITE))))) {
                int behindCandidateDestinationCoordinate = this.tileLoc_ + (-1 * 8);
                if (this.pieceColor_ == BLACK) {
                    behindCandidateDestinationCoordinate = this.tileLoc_ + (1 * 8);
                }
                if (!Board.board_[candidateDestinationCoordinate].isOccupied() &&
                        !Board.board_[behindCandidateDestinationCoordinate].isOccupied()) {
                    legalMoves.add(candidateDestinationCoordinate);
                }
            } else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.INSTANCE.EIGHTH_COLUMN.get(this.tileLoc_) && (this.pieceColor_ == WHITE)) ||
                            (BoardUtils.INSTANCE.FIRST_COLUMN.get(this.tileLoc_) && (this.pieceColor_ == BLACK)))) {
                if (Board.board_[candidateDestinationCoordinate].isOccupied()) {
                    if (this.pieceColor_ != Board.board_[candidateDestinationCoordinate].pieceColor_) {
                        if (this.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(candidateDestinationCoordinate);
                            Board.promotionCandidates[candidateDestinationCoordinate] = true;
                        } else {
                            legalMoves.add(candidateDestinationCoordinate);
                        }
                    }
                }
            } else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.INSTANCE.FIRST_COLUMN.get(this.tileLoc_) && (this.pieceColor_ == WHITE) ||
                            (BoardUtils.INSTANCE.EIGHTH_COLUMN.get(this.tileLoc_) && (this.pieceColor_ == BLACK))))) {
                if (Board.board_[candidateDestinationCoordinate].isOccupied()) {
                    if (this.pieceColor_ !=
                            Board.board_[candidateDestinationCoordinate].pieceColor_) {
                        if (this.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(candidateDestinationCoordinate);
                            Board.promotionCandidates[candidateDestinationCoordinate] = true;
                        } else {
                            legalMoves.add(candidateDestinationCoordinate);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    private boolean rookIsValid(int dest) {
        boolean retval = calculateRookLegalMoves().contains(dest);
        if (retval) {
            isFirstMove = false;
        }
        return retval;
    }

    private boolean bishopIsValid(int dest) {
        return calculateBishopLegalMoves().contains(dest);
    }

    private boolean kingIsValid(int dest) {
        boolean retval = calculateKingLegalMoves().contains(dest);
        if (retval) {
            if (isFirstMove) {
                isFirstMove = false;
            }
            if (pieceColor_ == WHITE) {
                Board.whiteKingLoc = dest;
            } else {
                Board.blackKingLoc = dest;
            }
        }
        return retval;
    }

    private boolean queenIsValid(int dest) {
        return calculateQueenLegalMoves().contains(dest);
    }

    private boolean knightIsValid(int dest) {
        return calculateKnightLegalMoves().contains(dest);
    }

    public Collection<Integer> calculateRookLegalMoves() {
        final List<Integer> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : ROOK_CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.tileLoc_;
            while (candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate <= 63) {
                if (isRookColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate <= 63) {
                    final int candidateDestinationCoord = Board.board_[candidateDestinationCoordinate].getLoc();
                    if (!Board.board_[candidateDestinationCoord].isOccupied()) {
                        legalMoves.add(candidateDestinationCoordinate);
                    } else {
                        if (this.pieceColor_ != Board.board_[candidateDestinationCoord].pieceColor_) {
                            legalMoves.add(candidateDestinationCoord);
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    public Collection<Integer> calculateQueenLegalMoves() {
        final List<Integer> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : QUEEN_CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.tileLoc_;
            while (candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate <= 63) {
                if (isQueenFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                        isQueenEightColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate <= 63) {
                    final int candidateDestinationCoord = Board.board_[candidateDestinationCoordinate].getLoc();
                    if (!Board.board_[candidateDestinationCoord].isOccupied()) {
                        legalMoves.add(candidateDestinationCoordinate);
                    } else {
                        if (this.pieceColor_ != Board.board_[candidateDestinationCoord].pieceColor_) {
                            legalMoves.add(candidateDestinationCoord);
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }

    public Collection<Integer> calculateKingLegalMoves() {
        final List<Integer> legalMoves = new ArrayList<>();
        for (final int currentCandidate : KING_CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoord = tileLoc_ + currentCandidate;

            if (candidateDestinationCoord <= 63 && candidateDestinationCoord >= 0) {
                if (!Board.board_[candidateDestinationCoord].isOccupied() ||
                        !pieceColor_.equals(Board.board_[candidateDestinationCoord].pieceColor_)) {
                    legalMoves.add(candidateDestinationCoord);
                }
            }
        }
        if (isFirstMove) {
            if (!Board.board_[tileLoc_ + 1].isOccupied()
                    && !Board.board_[tileLoc_ + 2].isOccupied()
                    && Board.board_[tileLoc_ + 3].isFirstMove) {
                legalMoves.add(tileLoc_ + 2);
            } else if (!Board.board_[tileLoc_ - 1].isOccupied()
                    && !Board.board_[tileLoc_ - 2].isOccupied()
                    && !Board.board_[tileLoc_ - 3].isOccupied()
                    && Board.board_[tileLoc_ - 4].isFirstMove) {
                legalMoves.add(tileLoc_ - 2);
            }
        }
        return legalMoves;
    }

    public Collection<Integer> calculateBishopLegalMoves() {
        final List<Integer> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : BISHOP_CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.tileLoc_;
            while (candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate <= 63) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                        isEighthColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate <= 63) {
                    final int candidateDestinationCoord = Board.board_[candidateDestinationCoordinate].getLoc();
                    if (!Board.board_[candidateDestinationCoord].isOccupied()) {
                        legalMoves.add(candidateDestinationCoordinate);
                    } else {
                        if (this.pieceColor_ != Board.board_[candidateDestinationCoord].pieceColor_) {
                            legalMoves.add(candidateDestinationCoord);
                        }
                        break;
                    }
                }
            }
        }

        return legalMoves;
    }

    public Collection<Integer> calculateKnightLegalMoves() {
        final List<Integer> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : KNIGHT_CANDIDATE_MOVE_COORDINATES) {
            if (isKnightFirstColumnExclusion(this.tileLoc_, currentCandidateOffset) ||
                    isSecondColumnExclusion(this.tileLoc_, currentCandidateOffset) ||
                    isSeventhColumnExclusion(this.tileLoc_, currentCandidateOffset) ||
                    isKnightEighthColumnExclusion(this.tileLoc_, currentCandidateOffset)) {
                continue;
            }
            final int candidateDestinationCoordinate = this.tileLoc_ + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (!Board.board_[candidateDestinationCoordinate].isOccupied()) {
                    legalMoves.add(candidateDestinationCoordinate);
                } else {
                    if (this.pieceColor_ != Board.board_[candidateDestinationCoordinate].pieceColor_) {
                        legalMoves.add(candidateDestinationCoordinate);
                    }
                }
            }
        }
        return legalMoves;
    }

    public int getId() {
        return id_;
    }

    public Color getColor() {
        return pieceColor_;
    }

    public Color getTileColor() {
        return tileColor_;
    }

    public void setColor(Color color) {
        pieceColor_ = color;
    }

    public String getName() {
        return pieceName_;
    }

    public boolean isWhiteTile() {
        return tileColor_ == WHITE;
    }

    public boolean isOccupied() {
        return !"empty".equals(pieceName_);
    }

    public int getLoc() {
        return tileLoc_;
    }

    private static boolean isRookColumnExclusion(final int currentCandidate,
                                                 final int candidateDestinationCoordinate) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(candidateDestinationCoordinate) && (currentCandidate == -1)) ||
                (BoardUtils.INSTANCE.EIGHTH_COLUMN.get(candidateDestinationCoordinate) && (currentCandidate == 1));
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(candidateDestinationCoordinate) &&
                ((currentCandidate == -9) || (currentCandidate == 7)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(candidateDestinationCoordinate) &&
                ((currentCandidate == -7) || (currentCandidate == 9));
    }

    private static boolean isQueenFirstColumnExclusion(final int currentPosition,
                                                       final int candidatePosition) {
        return BoardUtils.INSTANCE.FIRST_COLUMN.get(candidatePosition) && ((currentPosition == -9)
                || (currentPosition == -1) || (currentPosition == 7));
    }

    private static boolean isQueenEightColumnExclusion(final int currentPosition,
                                                       final int candidatePosition) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(candidatePosition) && ((currentPosition == -7)
                || (currentPosition == 1) || (currentPosition == 9));
    }

    // Knight column exclusions
    private static boolean isKnightFirstColumnExclusion(final int currentPosition,
                                                        final int candidateOffset) {
        return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentPosition) && ((candidateOffset == -17) ||
                (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.INSTANCE.SECOND_COLUMN.get(currentPosition) && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition,
                                                    final int candidateOffset) {
        return BoardUtils.INSTANCE.SEVENTH_COLUMN.get(currentPosition) && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isKnightEighthColumnExclusion(final int currentPosition,
                                                         final int candidateOffset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentPosition) && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }

    public boolean isPawnPromotionSquare(final int position) {
        if (this.pieceColor_ == WHITE) {
            return BoardUtils.INSTANCE.FIRST_ROW.get(position);
        } else {
            return BoardUtils.INSTANCE.EIGHTH_ROW.get(position);
        }
    }

    public void setName(String name) {
        pieceName_ = name;
    }

    private Color pieceColor_;
    private Color tileColor_;
    private String pieceName_;
    boolean isFirstMove;
    private int tileLoc_;
    private int id_;
}