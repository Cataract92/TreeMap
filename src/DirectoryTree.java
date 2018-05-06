
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.Vector;

public class DirectoryTree extends JFrame
{
	private static final int LINEAR = 0, LOG = 1, EXP = 2, SQRT = 3;

	private int width,height;
	private Dir files;
	private int scaleMode = LINEAR;
	private long now = System.currentTimeMillis();
	private long oldestAge = 0L;
	private long youngestAge = Long.MAX_VALUE;

	public static void main(String argv[])
	{
		String root;
		if (argv.length == 0)
			root = System.getProperty("user.dir");
		else
			root = argv[0];

		DirectoryTree tree = new DirectoryTree(1024,768,LINEAR);

		Dir d = tree.getDirectoryTree(root);
		tree.paint();
        d.print();
	}

	public DirectoryTree(int width, int height,int scaleMode) {
		super("TreeMap");
		this.width = width;
		this.height = height;
		this.scaleMode = scaleMode;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(width,height);
	}


	public void paint()
	{
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.PAGE_AXIS));
		Dimension d = new Dimension(width,height);

		jPanel.setBackground(Color.BLUE);
		jPanel.setPreferredSize(d);
		jPanel.setMinimumSize(d);
		jPanel.setMaximumSize(d);
		for (Entry e : files.files)
			traverse(e,files,jPanel,true);
		add(jPanel);
		setVisible(true);
	}

	private Color getGradiantColor(Color c1, Color c2, float p)
	{
		int r = Math.round(c1.getRed() * p + c2.getRed() * (1-p));
		int g = Math.round(c1.getGreen() * p + c2.getGreen() * (1-p));
		int b = Math.round(c1.getBlue() * p + c2.getBlue() * (1-p));
		return new Color(r,g,b);
	}

	private void traverse(Entry entry, Entry parent, JPanel parentJPanel, boolean doHorizontal)
	{
		JPanel jPanel = new JPanel();

		double height = parentJPanel.getPreferredSize().height;
		double width =  parentJPanel.getPreferredSize().width;

		if (doHorizontal){
			jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.LINE_AXIS));

			height = ( (entry.size * 1.d) / parent.size) * parentJPanel.getPreferredSize().height;
		} else {
			jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.PAGE_AXIS));
			width = ( (entry.size * 1.d) / parent.size) * parentJPanel.getPreferredSize().width;
		}

		Dimension d = new Dimension((int)Math.round(width),(int)Math.round(height));
		jPanel.setPreferredSize(d);
		jPanel.setMinimumSize(d);
		jPanel.setMaximumSize(d);

		if (entry instanceof Dir)
		{
            jPanel.setBackground(Color.BLUE);
			for (Entry e : ((Dir) entry).files)
			{
				traverse(e,entry,jPanel,!doHorizontal);
			}
		} else {
            jPanel.setBorder(LineBorder.createBlackLineBorder());
			Color color = getGradiantColor(Color.BLUE,Color.ORANGE,(( (entry.age - youngestAge) * 1f ) / (oldestAge - youngestAge)));
			jPanel.setBackground(color);
		}

		parentJPanel.add(jPanel);

	}
	/**
	 * Computes the directory tree starting at directory "startDir".
	 * A directory tree consists of inner nodes of type Dir and leaf nodes of type SingleFile.
	 * The size of each file and directory is computed using linear, logarithmic, etc. scale as 
	 * specified by the parameter "mode"
	 * @param startDir - starting at given directory
	 * @return
	 */
	public Dir getDirectoryTree(String startDir)
	{
		File f = new File(startDir);
		files = getDirectoryTree(new File[] { f });
		return files;
	}


	private Dir getDirectoryTree(File[] fa)
	{
		Vector<Entry> v = new Vector<Entry>();
		long size = 0;
		
		for (int i = 0; i < fa.length; i++)
		{
			File f = fa[i];
			if (f.isDirectory())
			{
				File[] sub_fa = f.listFiles();
				Dir df = getDirectoryTree(sub_fa);
				df.name = f.getName();
				v.add(df);
				size += df.size;
			}
			else
			{
				size += scale(f.length());
				long age = now - f.lastModified();
				if (age > oldestAge) {
					oldestAge = age;
				}
				if (age < youngestAge)
					youngestAge = age;
				v.add(new SingleFile(f.getName(), f, (int) scale(f.length()), age));
			}
		}
		
		return new Dir("", v, size);
	}


	public float scale(long i)
	{
		return scale((float) i);
	}


	public float scale(int i)
	{
		return scale((float) i);
	}


	public float scale(float f)
	{
		switch (scaleMode)
		{
			case LINEAR:
				return f;
			case LOG:
				return (float) (Math.log(1 + f));
			case EXP:
				return (float) Math.exp(f);
			case SQRT:
				return (float) Math.sqrt(f);
			default:
				return f;
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
} // end of class DirectoryTree
