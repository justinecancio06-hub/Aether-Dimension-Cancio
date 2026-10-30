using System;
using System.Drawing;
using System.Drawing.Imaging;

class Recolor {
    static void Main() {
        string path = @"D:\mc_mod\aether-dimension-template-26.1\src\main\resources\assets\aether-dimension\textures\block\aether_portal.png";
        using (Bitmap bmp = new Bitmap(path)) {
            for (int y = 0; y < bmp.Height; y++) {
                for (int x = 0; x < bmp.Width; x++) {
                    Color c = bmp.GetPixel(x, y);
                    if (c.A == 0) continue;
                    int nr = (int)(c.R * 0.4);
                    int ng = (int)(c.G * 0.7);
                    int nb = Math.Min(255, (int)(c.B * 2.8 + 70));
                    bmp.SetPixel(x, y, Color.FromArgb(c.A, nr, ng, nb));
                }
            }
            bmp.Save(path, ImageFormat.Png);
        }
        Console.WriteLine("done");
    }
}
