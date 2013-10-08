package ua.inf.krre.aprilbrush.logic;

import android.graphics.Bitmap;

import java.util.LinkedList;

import ua.inf.krre.aprilbrush.data.CanvasData;

public class UndoManager {
    private static final int UNDO_DEEP = 10;
    private static UndoManager undoManager = new UndoManager();
    private LinkedList<Bitmap> undoStack;
    private int stackPos = -1;

    private UndoManager() {
        undoStack = new LinkedList<Bitmap>();
    }

    public static UndoManager getInstance() {
        return undoManager;
    }

    public void undo() {
        if (stackPos - 1 >= 0) {
            CanvasData.getInstance().setBitmap(undoStack.get(--stackPos));
        }
    }

    public void redo() {
        if (stackPos + 1 < UNDO_DEEP & stackPos + 1 < undoStack.size()) {
            CanvasData.getInstance().setBitmap(undoStack.get(++stackPos));
        }
    }

    public void add(Bitmap bitmap) {
        // clear ending of the stack
        while (stackPos < undoStack.size() - 1) {
            undoStack.removeLast();
        }

        Bitmap undoBitmap = Bitmap.createBitmap(bitmap);
        undoStack.addLast(undoBitmap);
        if (undoStack.size() > UNDO_DEEP) {
            undoStack.removeFirst();
        } else {
            stackPos++;
        }
    }
}
