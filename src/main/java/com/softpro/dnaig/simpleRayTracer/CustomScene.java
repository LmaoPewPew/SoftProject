package com.softpro.dnaig.simpleRayTracer;


import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.objData.Face;
import com.softpro.dnaig.objData.Material;
import com.softpro.dnaig.objData.Vertex;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;

import java.io.IOException;
import java.util.ArrayList;

public class CustomScene {
    public static CustomScene scene;
    ArrayList<Object3D> objects = new ArrayList<Object3D>();
    public ArrayList<Light> lights = new ArrayList<Light>();
    public ArrayList<Entity> entities = new ArrayList<>();

    public CustomScene() throws IOException {

/*
        objects.add(new Sphere(5, new Vector3D(), Color.GREEN));
        objects.add(new Sphere(3, new Vector3D(10, 0, 0), Color.RED));
        objects.add(new Sphere(4, new Vector3D(-3, 3, 4), Color.BLUE));
        objects.add(new Sphere(3, new Vector3D(2, -4, 7), Color.YELLOW));



 */
        /*
        objects.add(CoordinateSystem.getCoordinateSystem());


        objects.add(new Triangle(new Vector3D(-10, -10, 10), new Vector3D(0, 10, 0), new Vector3D(10, -10, 10), Color.BLUE));
        objects.add(new Triangle(new Vector3D(-10, -10, 10), new Vector3D(0, 10, 0), new Vector3D(-10, -10, -10), Color.BLUE));
        objects.add(new Triangle(new Vector3D(-10, -10, -10), new Vector3D(0, 10, 0), new Vector3D(10, -10, -10), Color.BLUE));
        objects.add(new Triangle(new Vector3D(10, -10, -10), new Vector3D(0, 10, 0), new Vector3D(10, -10, 10), Color.BLUE));




         */
        com.softpro.dnaig.utils.Vector3D v1 = new com.softpro.dnaig.utils.Vector3D(-1,-1,1);
        com.softpro.dnaig.utils.Vector3D v2 = new com.softpro.dnaig.utils.Vector3D(0,1,0);
        com.softpro.dnaig.utils.Vector3D v3 = new com.softpro.dnaig.utils.Vector3D(3,-1,2);
        Vertex vertex1 = new Vertex(v1, v1, v1);
        Vertex vertex2 = new Vertex(v2, v2, v2);
        Vertex vertex3 = new Vertex(v3, v3, v3);
        Vertex[] vertices = {vertex1, vertex2, vertex3};
        Face face = new Face(vertices, new Material(), 0);
        ArrayList<Face> faceArrayList = new ArrayList<>();
        faceArrayList.add(face);
        Entity entity2 = new Entity("Test", faceArrayList, 3);
        Entity entity = ObjFileReader.createObject("C:\\Users\\Leon\\Downloads\\cube.obj");
        //entity.scale(10);
        entities.add(entity);
        lights.add(new PointLight(new Vector3D(15, 15, 15), new Vector3D(50, 50, 50)));
        lights.add(new PointLight(new Vector3D(0, 2, -2), new Vector3D(20, 20, 20)));
        lights.add(new PointLight(new Vector3D(-15, 15, -15), new Vector3D(50, 50, 50)));
        lights.add(new PointLight(new Vector3D(-15, 15, 15), new Vector3D(50, 50, 50)));
    }

    public static CustomScene getScene() throws IOException {
        if(scene==null){
            scene = new CustomScene();
        }
        return scene;
    }

    public void addEntity(Entity entity, com.softpro.dnaig.utils.Vector3D position){
        entities.add(entity);
    }
}
