import java.io.*;
public class cel331 {
    public static void main(String args[] ) throws Exception {
        cel331 obj=new cel331();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the number of bays");
		int m = Integer.parseInt(br.readLine());
        System.out.println("Enter the number of storeys");
		int n = Integer.parseInt(br.readLine());
		double load[]=new double[n];
		for (int i=0;i<n;i++){
			System.out.println("Enter the loading in KN/m for n="+(i+1));
			load[i]=Double.parseDouble(br.readLine());
		}
		System.out.println("Enter bay length in metre:");
		double l=Double.parseDouble(br.readLine());
		System.out.println("Enter storey height in metre:");
		double h=Double.parseDouble(br.readLine());
		double beam[]=new double[3];
		double col[]=new double[3];
		System.out.println("Enter Area of beam:");
		beam[0]=Double.parseDouble(br.readLine());
		System.out.println("Enter Inertia of beam:");
		beam[1]=Double.parseDouble(br.readLine());
		System.out.println("Enter Elastic Modulus of beam:");
		beam[2]=Double.parseDouble(br.readLine());
		System.out.println("Enter Area of column:");
		col[0]=Double.parseDouble(br.readLine());
		System.out.println("Enter Inertia of column:");
		col[1]=Double.parseDouble(br.readLine());
		System.out.println("Enter Elastic Modulus of column:");
		col[2]=Double.parseDouble(br.readLine());
		obj.dsm(m+1,n+1,load,l,h,beam,col);
	}
	
	public void dsm(int m, int n, double load[], double l, double h, double beam[], double col[]){
		assignmentA ps = new assignmentA(m, n, l, h, beam, col);
		ps.flush();
		
		//******* when m<n *******
		
		if(m<n){
			for (int i=1;i<m*(n-1);i++){
				if (i%m==0)
					continue;
				else
					ps.localH(i, load[i/m]);
			}
			
			for (int i=1;i<=((n-1)*m);i++)
				ps.localV(i);
		}		
					
		//********* when m>=n *******
		
		else{
		for (int i=1;i<=(m-1)*(n-1);i++)
			ps.localH(i, load[(i-1)%(n-1)]);
		
		for (int i=1;i<=((n-1)*m);i++)
			ps.localV(i);	
		}
		
		double KTS[][]=ps.KTSMatrix();
		double P[]=ps.PMatrix();
		double Kpp[][]=ps.computeKppMatrix(KTS);
		double V[][]=ps.computeVMatrix(Kpp);
		double VT[][]=ps.transpose(V);
		double W[]=ps.computeMatrix(VT,P,1);
		double U[]=ps.computeMatrix(V,W,2);
		double Kxp[][]=ps.computeKxpMatrix(KTS);
		double X[]=ps.computeX(Kxp,U);
		
		export(KTS,ps.matrixSize(KTS),ps.matrixSize(KTS),"KTS.xlt");
		export(Kpp,3*(m*n-m),3*(m*n-m),"Kpp.xlt");
		export(Kxp,3*m,3*(m*n-m),"Kxp.xlt");
		
		//check
		double sum1=0;
		double sum2=0;
		for (int i=0;i<3*m;i++){
			if (i%3==0)
				sum1+=X[i];
			else if (i%3==1)
				sum2+=X[i];
		}
		System.out.println("H"+sum1);
		System.out.println("V"+sum2);
		
		
		try{//printing X
			BufferedWriter bw = new BufferedWriter(new FileWriter("X.xlt"));
			//bw.write("Joint"+"\t"+"\t"+"Horizontal Reaction (KN)"+"\t"+"Vertical Reaction (KN)"+"\t"+"Moment(KN.m)");
			bw.newLine();
			bw.newLine();
			for (int i=3*(m*n-m)+1;i<=3*m*n;i++){
				if (i%3==1)
					bw.write( ((i-1)/3)+"\t"+"\t"+X[i-3*(m*n-m)+1]+"\t" );
				else if (i%3==2)
					bw.write(X[i-3*(m*n-m)+1]+"\t");
				else if (i%3==0){
					bw.write(X[i-3*(m*n-m)+1]+"\t");
					bw.newLine();
				}	
			}
		bw.close();
		}
		catch(Exception e){}
		
	}

	public void export(double a[][], int x, int y, String s){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(s));
			bw.write("\t"+"\t");
			for (int i=0;i<y;i++)
				bw.write(i+"\t");
			bw.newLine();
			bw.newLine();
			for (int i=0;i<x;i++){
				bw.write(i+"\t"+"\t");
				for (int j=0;j<y;j++)
					bw.write(a[i][j]+"\t");
				bw.newLine();
			}
			bw.close();
		}
		catch(Exception e){}
	}
}
