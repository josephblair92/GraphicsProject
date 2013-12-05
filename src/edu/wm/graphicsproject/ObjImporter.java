package edu.wm.graphicsproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.widget.Toast;

public class ObjImporter {
	
	Context c;
	
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
        -0.5f, -0.5f, 0.0f,   // bottom left
         0.5f, -0.5f, 0.0f,   // bottom right
         0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	
	public ObjImporter(Context c) {
		this.c = c;
	}
	
	public Mesh getMeshFromObj(int resID) {
		
		InputStream is = c.getResources().openRawResource(resID);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		ArrayList vertices = new ArrayList<Float>();
		ArrayList faces = new ArrayList<Short>();
		ArrayList vertexNormals = new ArrayList<Short>();
		
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				if (st.hasMoreElements())  {
					String type = st.nextToken();
					
					if (type.equals("v"))  {

						while (st.hasMoreElements())  {
							String str = st.nextToken();
							vertices.add(Float.parseFloat(str));
						}
						
					}
					
					if (type.equals("f"))  {

						while (st.hasMoreElements())  {
							String str = st.nextToken();
							int index = str.indexOf('/');
							faces.add(Short.parseShort(str.substring(0, index)));
							vertexNormals.add(Short.parseShort(str.substring(index+1)));
						}
						
					}
					
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		float [] verticesArray = new float [vertices.size()];
		for (int i = 0; i < vertices.size(); i++)  {
			verticesArray[i] = (Float) vertices.get(i);
			//System.out.print(verticesArray[i] + " ");
		}
		
		//System.out.println();

		short [] facesArray = new short [faces.size()];
		for (int i = 0; i < faces.size(); i++)  {
			Short s = (Short)faces.get(i);
			s--;
			facesArray[i] = s;
			//System.out.println(facesArray[i] + " ");
		}
		
		short [] vertexNormalsArray = new short [vertexNormals.size()];
		for (int i = 0; i < vertexNormals.size(); i++)  {
			Short s = (Short)vertexNormals.get(i);
			s--;
			vertexNormalsArray[i] = s;
		}
		
		return new Mesh(verticesArray, facesArray);
	}

}
