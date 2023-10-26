package com.dnaig.dnaig.objData;

public class Face {
    private Vertex[] vertices;  // array of vertices that make up the face

    // default constructor initializes face
    public Face(){
        this.vertices = new Vertex[]{
                new Vertex(),
                new Vertex(),
                new Vertex()
        };
    }

    // constructor sets the vertices of the face
    public Face(Vertex[] vertices){
        this.vertices = vertices;
    }

    // get number of vertices of the face
    public int getVerticeCount(){
        return this.vertices.length;
    }

    // string representation of the face
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (Vertex vertex : vertices) {
            str.append(
                    String.format(
                            "vert %d\n%s\n", ++i, vertex)
            );
        }

        return str.toString();
    }
}
