import java.io.File;

class SingleFile extends Entry
{
	File file;


	SingleFile(String n, File f, long s, long a)
	{
		name = n;
		file = f;
		size = s;
		age = a;
	}


	void print(String prefix)
	{
		System.out.println(prefix + name + ":" + size);
		fullname = prefix + name + ":" + size;
	}
} // end of class SingleFile