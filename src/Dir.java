
import java.util.Vector;

class Dir extends Entry
{
	Vector<Entry> files;


	Dir(String n, Vector<Entry> fs, long s)
	{
		name = n;
		files = fs;
		size = s;
	}


	void print(String prefix)
	{
		System.out.println(prefix + name + ":" + size);
		for (int i = 0; i < files.size(); i++)
		{
			((Entry) files.elementAt(i)).print(prefix + name + "\\");
		}
		fullname = prefix + name + ":" + size;
	}
} // end of class Dir